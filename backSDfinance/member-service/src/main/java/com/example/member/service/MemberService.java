package com.example.member.service;

import com.example.common.model.UserRole;
import com.example.common.security.SecurityContextHelper;
import com.example.member.dto.CreateMemberRequest;
import com.example.member.dto.MemberDto;
import com.example.member.dto.MemberSummaryDto;
import com.example.member.dto.UpdateMemberRequest;
import com.example.member.model.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SecurityContextHelper securityHelper;

    public List<MemberDto> getAllMembers() {
        UserRole role = securityHelper.getCurrentRole();

        // ADMIN, TRESORIER, MEMBRE n'ont pas accès à la liste globale des membres
        if (role == UserRole.ADMIN || role == UserRole.TRESORIER_CAISSIER || role == UserRole.MEMBRE) {
            throw new AccessDeniedException("Accès refusé aux données membres");
        }
        
        List<Member> members;
        if (role == UserRole.LEADER) {
            // Le Leader récupère tous les membres triés alphabétiquement
            members = memberRepository.findAll(org.springframework.data.domain.Sort.by("nom"));
        } else {
            // PRESIDENT : filtre sur ses groupes uniquement
            members = memberRepository.findByGroupeIdIn(securityHelper.getCurrentGroupIds());
        }
        
        return members.stream().map(this::toDto).collect(Collectors.toList());
    }

    /** Vue sommaire pour le LEADER — une seule requête agrégée */
    public List<MemberSummaryDto> getMembersSummary() {
        securityHelper.requireRole(UserRole.LEADER);
        return memberRepository.findMemberCountByGroupe();
    }

    public MemberDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));
        // Vérifier que le membre appartient à un groupe accessible
        securityHelper.requireGroupeAccess(member.getGroupeId());
        return toDto(member);
    }

    @Transactional
    public MemberDto createMember(CreateMemberRequest request) {
        // Seul le PRESIDENT peut créer des membres
        securityHelper.requireRole(UserRole.PRESIDENT);
        // Vérifier que le groupe cible fait partie des groupes du PRESIDENT
        securityHelper.requireGroupeAccess(request.getGroupeId());

        Member member = Member.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .sexe(request.getSexe())
                .statut(request.getStatut())
                .telephone(request.getTelephone())
                .email(request.getEmail())
                .adresse(request.getAdresse())
                .groupeId(request.getGroupeId())
                .build();
        return toDto(memberRepository.save(member));
    }

    @Transactional
    public MemberDto updateMember(Long id, UpdateMemberRequest request) {
        securityHelper.requireRole(UserRole.PRESIDENT);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));
        securityHelper.requireGroupeAccess(member.getGroupeId());

        member.setNom(request.getNom());
        member.setPrenom(request.getPrenom());
        member.setSexe(request.getSexe());
        member.setStatut(request.getStatut());
        member.setTelephone(request.getTelephone());
        member.setEmail(request.getEmail());
        member.setAdresse(request.getAdresse());
        return toDto(memberRepository.save(member));
    }

    @Transactional
    public void deleteMember(Long id) {
        securityHelper.requireRole(UserRole.PRESIDENT);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));
        securityHelper.requireGroupeAccess(member.getGroupeId());
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
                .groupeId(member.getGroupeId())
                .build();
    }
}
