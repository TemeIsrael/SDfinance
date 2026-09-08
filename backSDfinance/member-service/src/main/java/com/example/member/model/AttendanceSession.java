package com.example.member.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendance_sessions", indexes = {
        @Index(name = "idx_att_session_groupe", columnList = "groupe_id"),
        @Index(name = "idx_att_session_date", columnList = "date_session")
})
public class AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_culte", nullable = false)
    private TypeCulte typeCulte;

    @Column(name = "date_session", nullable = false)
    private LocalDateTime dateSession;

    @Column(name = "groupe_id", nullable = false)
    private Long groupeId;

    @Column(nullable = false)
    @Builder.Default
    private boolean cloturee = false;

    private String remarque;

    /** Libellé spécifique si typeCulte == AUTRE */
    private String libelle;
}
