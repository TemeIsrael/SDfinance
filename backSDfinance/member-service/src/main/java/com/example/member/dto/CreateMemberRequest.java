package com.example.member.dto;

import com.example.member.model.Sexe;
import com.example.member.model.StatutMembre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberRequest {
    private String nom;
    private String prenom;
    private Sexe sexe;
    private StatutMembre statut;
    private String telephone;
    private String email;
    private String adresse;
    private String photoUrl;
    private Long groupeId;
    private LocalDate dateAjout;
}
