package com.example.member.dto;

import com.example.member.model.StatutPresence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordDto {

    private Long id;
    private Long sessionId;
    private Long memberId;
    private String memberNom;
    private String memberPrenom;
    private StatutPresence statut;
    private LocalDateTime heurePointage;
    private String remarque;
}
