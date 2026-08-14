package com.example.finance.service;

import com.example.common.model.UserRole;
import com.example.common.security.SecurityContextHelper;
import com.example.finance.dto.AccountDto;
import com.example.finance.dto.CreateTransactionRequest;
import com.example.finance.dto.FinanceSummaryDto;
import com.example.finance.dto.TransactionDto;
import com.example.finance.model.Account;
import com.example.finance.model.Transaction;
import com.example.finance.repository.AccountRepository;
import com.example.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final SecurityContextHelper securityHelper;

    public List<AccountDto> getAccounts() {
        UserRole role = securityHelper.getCurrentRole();
        if (role == UserRole.ADMIN) {
            throw new AccessDeniedException("Accès refusé aux données financières pour l'ADMIN");
        }
        
        List<Account> accounts;
        if (role == UserRole.LEADER) {
            accounts = accountRepository.findAll();
        } else if (role == UserRole.MEMBRE) {
            accounts = accountRepository.findByOwnerUsername(securityHelper.getCurrentUsername());
        } else {
            accounts = accountRepository.findByGroupeIdIn(securityHelper.getCurrentGroupIds());
        }
        
        return accounts.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<TransactionDto> getTransactions() {
        UserRole role = securityHelper.getCurrentRole();
        if (role == UserRole.ADMIN) {
            throw new AccessDeniedException("Accès refusé");
        }
        
        List<Transaction> transactions;
        if (role == UserRole.LEADER) {
            transactions = transactionRepository.findAll();
        } else if (role == UserRole.MEMBRE) {
            transactions = transactionRepository.findByAccountOwnerUsername(securityHelper.getCurrentUsername());
        } else {
            transactions = transactionRepository.findByGroupeIdIn(securityHelper.getCurrentGroupIds());
        }
        
        return transactions.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<FinanceSummaryDto> getFinanceSummary() {
        securityHelper.requireRole(UserRole.LEADER);
        return transactionRepository.getFinanceSummaryByGroupe();
    }

    @Transactional
    public TransactionDto addTransaction(CreateTransactionRequest request) {
        // TRESORIER seul a le droit d'écriture sur les transactions
        securityHelper.requireRole(UserRole.TRESORIER_CAISSIER);
        securityHelper.requireGroupeAccess(request.getGroupeId());

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getGroupeId().equals(request.getGroupeId())) {
            throw new AccessDeniedException("Le compte n'appartient pas à ce groupe");
        }

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(request.getType())
                .amount(request.getAmount())
                .description(request.getDescription())
                .groupeId(request.getGroupeId())
                .build();

        if (transaction.getType() == Transaction.Type.CREDIT) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }

        accountRepository.save(account);
        return toDto(transactionRepository.save(transaction));
    }

    private AccountDto toDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .iban(account.getIban())
                .ownerUsername(account.getOwnerUsername())
                .balance(account.getBalance())
                .transactionCount(account.getTransactions().size())
                .groupeId(account.getGroupeId())
                .build();
    }

    private TransactionDto toDto(Transaction t) {
        return TransactionDto.builder()
                .id(t.getId())
                .accountId(t.getAccount().getId())
                .type(t.getType())
                .amount(t.getAmount())
                .description(t.getDescription())
                .createdAt(t.getCreatedAt())
                .groupeId(t.getGroupeId())
                .build();
    }
}
