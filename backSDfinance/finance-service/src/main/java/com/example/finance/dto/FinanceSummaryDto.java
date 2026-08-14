package com.example.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO utilisé exclusivement par le LEADER pour la vue sommaire financière.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinanceSummaryDto {
    private Long groupeId;
    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
    private BigDecimal balance; // Optionnel : credit - debit
}
