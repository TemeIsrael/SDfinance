package com.example.finance.dto;

import com.example.finance.model.FinanceRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceRecordDto {
    private Long id;
    private FinanceRecord.RecordType typeRecord;
    private String caisseId;
    private BigDecimal montant;
    private LocalDate dateOperation;
    private String description;
    private String membreNom;
    private Long membreGroupeId;
    private Long groupeId;
    private LocalDateTime createdAt;
}
