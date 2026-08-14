package com.example.finance.repository;

import com.example.common.repository.GroupScopedRepository;
import com.example.finance.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends GroupScopedRepository<Transaction, Long> {

    /**
     * Requête agrégée utilisée par le LEADER pour avoir un résumé financier par groupe.
     * sum(case when type = 'CREDIT' then amount else 0 end) as totalCredit
     */
    @Query("SELECT new com.example.finance.dto.FinanceSummaryDto(" +
           "t.groupeId, " +
           "SUM(CASE WHEN t.type = com.example.finance.model.Transaction.Type.CREDIT THEN t.amount ELSE 0 END), " +
           "SUM(CASE WHEN t.type = com.example.finance.model.Transaction.Type.DEBIT THEN t.amount ELSE 0 END), " +
           "SUM(CASE WHEN t.type = com.example.finance.model.Transaction.Type.CREDIT THEN t.amount ELSE -t.amount END)) " +
           "FROM Transaction t GROUP BY t.groupeId")
    List<com.example.finance.dto.FinanceSummaryDto> getFinanceSummaryByGroupe();

    List<Transaction> findByAccountOwnerUsername(String ownerUsername);
}
