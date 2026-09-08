package com.example.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStatsDto {

    private Long groupeId;
    private long totalSessions;
    private long totalPresents;
    private long totalAbsents;
    private long totalNonRenseignes;
    private double tauxPresencePercentage;
}
