package com.example.finance.service;

import com.example.common.model.UserRole;
import com.example.common.security.SecurityContextHelper;
import com.example.finance.dto.CreateFinanceRecordRequest;
import com.example.finance.dto.FinanceRecordDto;
import com.example.finance.model.FinanceRecord;
import com.example.finance.repository.FinanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final FinanceRecordRepository financeRecordRepository;
    private final SecurityContextHelper securityHelper;

    public List<FinanceRecordDto> getRecords() {
        UserRole role = securityHelper.getCurrentRole();
        if (role == UserRole.ADMIN) {
            throw new AccessDeniedException("Accès refusé aux données financières pour l'ADMIN");
        }
        
        List<FinanceRecord> records;
        if (role == UserRole.LEADER) {
            records = financeRecordRepository.findAll();
        } else if (role == UserRole.MEMBRE) {
            // Un membre ne voit que ses propres paiements/épargnes
            records = financeRecordRepository.findByMembreNomIgnoreCase(securityHelper.getCurrentUsername());
        } else {
            // PRESIDENT, TRESORIER, COLLABORATOR
            records = financeRecordRepository.findByGroupeIdIn(securityHelper.getCurrentGroupIds());
        }
        
        return records.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public FinanceRecordDto addRecord(CreateFinanceRecordRequest request) {
        // TRESORIER ou PRESIDENT peuvent ajouter (selon le besoin, mais gardons TRESORIER_CAISSIER par défaut ou ceux ayant accès au groupe)
        // Vérification de base : l'utilisateur doit avoir accès au groupe s'il n'est pas LEADER
        if (securityHelper.getCurrentRole() != UserRole.LEADER) {
            securityHelper.requireGroupeAccess(request.getGroupeId());
        }

        FinanceRecord record = FinanceRecord.builder()
                .typeRecord(request.getTypeRecord())
                .caisseId(request.getCaisseId())
                .montant(request.getMontant())
                .dateOperation(request.getDateOperation() != null ? request.getDateOperation() : java.time.LocalDate.now())
                .description(request.getDescription())
                .membreNom(request.getMembreNom())
                .membreGroupeId(request.getMembreGroupeId())
                .groupeId(request.getGroupeId())
                .build();

        return toDto(financeRecordRepository.save(record));
    }

    private FinanceRecordDto toDto(FinanceRecord record) {
        return FinanceRecordDto.builder()
                .id(record.getId())
                .typeRecord(record.getTypeRecord())
                .caisseId(record.getCaisseId())
                .montant(record.getMontant())
                .dateOperation(record.getDateOperation())
                .description(record.getDescription())
                .membreNom(record.getMembreNom())
                .membreGroupeId(record.getMembreGroupeId())
                .groupeId(record.getGroupeId())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
