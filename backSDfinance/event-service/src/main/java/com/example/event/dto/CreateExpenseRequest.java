package com.example.event.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateExpenseRequest {
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
}
