package com.example.finance.dto;

import com.example.finance.model.Transaction.Type;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {
    private Long accountId;
    private Type type;
    private BigDecimal amount;
    private String description;
    private Long groupeId;
}
