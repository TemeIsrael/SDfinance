package com.example.finance.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_records", indexes = {
        @Index(name = "idx_finance_groupe", columnList = "groupe_id"),
        @Index(name = "idx_finance_caisse", columnList = "caisse_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum RecordType {
        PAIEMENT_MEMBRE, ENTREE_CAISSE, SORTIE_CAISSE
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType typeRecord;

    @Column(name = "caisse_id", nullable = false)
    private String caisseId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal montant;

    @Column(name = "date_operation", nullable = false)
    private LocalDate dateOperation;

    @Column(length = 512)
    private String description;

    @Column(name = "membre_nom")
    private String membreNom;

    @Column(name = "membre_groupe_id")
    private Long membreGroupeId;

    @Column(name = "groupe_id")
    private Long groupeId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
