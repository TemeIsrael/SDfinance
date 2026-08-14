package com.example.member.repository;

import com.example.common.repository.GroupScopedRepository;
import com.example.member.dto.MemberSummaryDto;
import com.example.member.model.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends GroupScopedRepository<Member, Long> {

    /**
     * Requête agrégée : nombre de membres par groupe.
     * Utilisée UNIQUEMENT par le LEADER pour sa vue sommaire.
     * Performance : un seul SELECT GROUP BY au lieu de charger toutes les lignes.
     */
    @Query("SELECT new com.example.member.dto.MemberSummaryDto(m.groupeId, COUNT(m)) " +
           "FROM Member m GROUP BY m.groupeId")
    List<MemberSummaryDto> findMemberCountByGroupe();
}
