package com.example.event.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for updating an existing expense.
 */
@Data
public class UpdateExpenseRequest {
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
}
