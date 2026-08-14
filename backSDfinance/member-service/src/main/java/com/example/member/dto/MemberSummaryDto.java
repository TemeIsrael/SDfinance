package com.example.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilisé exclusivement par le LEADER pour la vue sommaire.
 * Construit directement par une requête JPQL agrégée → performant.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSummaryDto {
    private Long groupeId;
    private Long nombreMembres;
}
