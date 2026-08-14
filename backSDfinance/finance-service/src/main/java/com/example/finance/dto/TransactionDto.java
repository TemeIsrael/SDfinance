package com.example.finance.dto;

import com.example.finance.model.Transaction.Type;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private Long accountId;
    private Type type;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
    private Long groupeId;
}
