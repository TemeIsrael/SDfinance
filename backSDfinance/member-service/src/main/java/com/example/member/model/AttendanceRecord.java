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
@Table(name = "attendance_records", indexes = {
        @Index(name = "idx_att_record_session", columnList = "session_id"),
        @Index(name = "idx_att_record_member", columnList = "member_id"),
        @Index(name = "idx_att_record_session_member", columnList = "session_id, member_id", unique = true)
})
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutPresence statut = StatutPresence.NON_RENSEIGNE;

    @Column(name = "heure_pointage")
    private LocalDateTime heurePointage;

    private String remarque;
}
