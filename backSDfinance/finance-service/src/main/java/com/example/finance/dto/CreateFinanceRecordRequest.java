package com.example.finance.dto;

import com.example.finance.model.FinanceRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFinanceRecordRequest {
    private FinanceRecord.RecordType typeRecord;
    private String caisseId;
    private BigDecimal montant;
    private LocalDate dateOperation;
    private String description;
    private String membreNom;
    private Long membreGroupeId;
    private Long groupeId;
}
