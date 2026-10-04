package com.example.member.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "members", indexes = {
        @Index(name = "idx_member_groupe", columnList = "groupe_id")
})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sexe sexe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutMembre statut;

    private String telephone;
    private String email;
    private String adresse;

    @Column(name = "photo_url")
    private String photoUrl;

    /** Date d'ajout du membre (saisie manuelle ou auto) */
    @Column(name = "date_ajout")
    private LocalDate dateAjout;

    /** Clé étrangère vers le groupe : filtrage RBAC */
    @Column(name = "groupe_id")
    private Long groupeId;
}
