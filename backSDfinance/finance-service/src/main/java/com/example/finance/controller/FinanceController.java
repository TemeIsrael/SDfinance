package com.example.finance.controller;

import com.example.finance.dto.CreateFinanceRecordRequest;
import com.example.finance.dto.FinanceRecordDto;
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

    @GetMapping("/records")
    @PreAuthorize("hasAnyAuthority('PRESIDENT', 'TRESORIER_CAISSIER', 'LEADER', 'MEMBRE', 'COLLABORATOR')")
    public List<FinanceRecordDto> getRecords() {
        return financeService.getRecords();
    }

    @PostMapping("/records")
    @PreAuthorize("hasAnyAuthority('PRESIDENT', 'TRESORIER_CAISSIER', 'LEADER', 'COLLABORATOR')")
    public ResponseEntity<FinanceRecordDto> addRecord(@RequestBody CreateFinanceRecordRequest request) {
        return ResponseEntity.ok(financeService.addRecord(request));
    }
}
