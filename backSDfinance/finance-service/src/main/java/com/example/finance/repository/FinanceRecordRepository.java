package com.example.finance.repository;

import com.example.common.repository.GroupScopedRepository;
import com.example.finance.model.FinanceRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceRecordRepository extends GroupScopedRepository<FinanceRecord, Long> {
    List<FinanceRecord> findByMembreNomIgnoreCase(String membreNom);
}
