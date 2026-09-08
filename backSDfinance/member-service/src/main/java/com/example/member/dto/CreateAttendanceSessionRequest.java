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
public class CreateAttendanceSessionRequest {

    private String titre;
    private TypeCulte typeCulte;
    private LocalDateTime dateSession;
    private Long groupeId;
    private String remarque;
    private String libelle;
}
