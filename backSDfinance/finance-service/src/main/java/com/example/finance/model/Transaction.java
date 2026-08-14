package com.example.finance.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_transaction_groupe", columnList = "groupe_id"),
        @Index(name = "idx_transaction_groupe_type", columnList = "groupe_id, type")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public enum Type {
        CREDIT, DEBIT
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(length = 512)
    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "groupe_id")
    private Long groupeId;
}
