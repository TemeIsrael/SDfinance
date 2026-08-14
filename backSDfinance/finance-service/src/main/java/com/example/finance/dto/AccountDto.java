package com.example.finance.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String iban;
    private String ownerUsername;
    private BigDecimal balance;
    private int transactionCount;
    private Long groupeId;
}
