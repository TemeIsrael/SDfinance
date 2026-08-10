package com.example.member.dto;

import com.example.member.model.Sexe;
import com.example.member.model.StatutMembre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRequest {
    private String nom;
    private String prenom;
    private Sexe sexe;
    private StatutMembre statut;
    private String telephone;
    private String email;
    private String adresse;
}
