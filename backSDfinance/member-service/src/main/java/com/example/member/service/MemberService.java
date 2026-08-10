package com.example.member.service;

import com.example.member.dto.CreateMemberRequest;
import com.example.member.dto.MemberDto;
import com.example.member.dto.UpdateMemberRequest;
import com.example.member.model.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberDto> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public MemberDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));
        return toDto(member);
    }

    @Transactional
    public MemberDto createMember(CreateMemberRequest request) {
        Member member = Member.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .sexe(request.getSexe())
                .statut(request.getStatut())
                .telephone(request.getTelephone())
                .email(request.getEmail())
                .adresse(request.getAdresse())
                .build();
        Member saved = memberRepository.save(member);
        return toDto(saved);
    }

    @Transactional
    public MemberDto updateMember(Long id, UpdateMemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));
        member.setNom(request.getNom());
        member.setPrenom(request.getPrenom());
        member.setSexe(request.getSexe());
        member.setStatut(request.getStatut());
        member.setTelephone(request.getTelephone());
        member.setEmail(request.getEmail());
        member.setAdresse(request.getAdresse());
        Member saved = memberRepository.save(member);
        return toDto(saved);
    }

    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new RuntimeException("Member not found: " + id);
        }
        memberRepository.deleteById(id);
    }

    private MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .nom(member.getNom())
                .prenom(member.getPrenom())
                .sexe(member.getSexe())
                .statut(member.getStatut())
                .telephone(member.getTelephone())
                .email(member.getEmail())
                .adresse(member.getAdresse())
                .build();
    }
}
