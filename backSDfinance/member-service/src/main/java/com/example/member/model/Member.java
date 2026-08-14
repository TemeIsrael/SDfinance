package com.example.member.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    /** Clé étrangère vers le groupe : filtrage RBAC */
    @Column(name = "groupe_id")
    private Long groupeId;
}
