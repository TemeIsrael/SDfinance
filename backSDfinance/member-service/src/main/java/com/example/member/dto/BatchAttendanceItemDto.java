package com.example.member.dto;

import com.example.member.model.StatutPresence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchAttendanceItemDto {
    private Long memberId;
    private StatutPresence statut;
    private String remarque;
}
