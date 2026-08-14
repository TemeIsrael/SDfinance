package com.example.member.controller;

import com.example.member.dto.CreateMemberRequest;
import com.example.member.dto.MemberDto;
import com.example.member.dto.MemberSummaryDto;
import com.example.member.dto.UpdateMemberRequest;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('PRESIDENT', 'LEADER')")
    public ResponseEntity<List<MemberDto>> getAll() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('LEADER')")
    public ResponseEntity<List<MemberSummaryDto>> getSummary() {
        return ResponseEntity.ok(memberService.getMembersSummary());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESIDENT')")
    public ResponseEntity<MemberDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRESIDENT')")
    public ResponseEntity<MemberDto> create(@RequestBody CreateMemberRequest request) {
        return ResponseEntity.ok(memberService.createMember(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESIDENT')")
    public ResponseEntity<MemberDto> update(@PathVariable Long id, @RequestBody UpdateMemberRequest request) {
        return ResponseEntity.ok(memberService.updateMember(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESIDENT')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
