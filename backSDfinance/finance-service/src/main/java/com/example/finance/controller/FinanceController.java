package com.example.finance.controller;

import com.example.finance.dto.AccountDto;
import com.example.finance.dto.CreateTransactionRequest;
import com.example.finance.dto.FinanceSummaryDto;
import com.example.finance.dto.TransactionDto;
import com.example.finance.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/accounts")
    @PreAuthorize("hasAnyAuthority('PRESIDENT', 'TRESORIER_CAISSIER', 'LEADER', 'MEMBRE')")
    public List<AccountDto> getAccounts() {
        return financeService.getAccounts();
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasAnyAuthority('PRESIDENT', 'TRESORIER_CAISSIER', 'LEADER', 'MEMBRE')")
    public List<TransactionDto> getTransactions() {
        return financeService.getTransactions();
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('LEADER')")
    public ResponseEntity<List<FinanceSummaryDto>> getSummary() {
        return ResponseEntity.ok(financeService.getFinanceSummary());
    }

    @PostMapping("/transactions")
    @PreAuthorize("hasAuthority('TRESORIER_CAISSIER')")
    public ResponseEntity<TransactionDto> addTransaction(@RequestBody CreateTransactionRequest request) {
        return ResponseEntity.ok(financeService.addTransaction(request));
    }
}
