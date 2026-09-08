package com.example.member.dto;

import com.example.member.model.TypeCulte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSessionDto {

    private Long id;
    private String titre;
    private TypeCulte typeCulte;
    private LocalDateTime dateSession;
    private Long groupeId;
    private boolean cloturee;
    private String remarque;
    private String libelle;
    private long totalMembres;
    private long totalPresents;
    private long totalAbsents;
    private long totalNonRenseignes;
}
