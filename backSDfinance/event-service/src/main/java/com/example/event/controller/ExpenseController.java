package com.example.event.controller;

import com.example.event.dto.*;
import com.example.event.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller exposing expense management for events.
 */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    // ---------- READ ----------
    @GetMapping("/{eventId}/expenses")
    @PreAuthorize("hasAnyAuthority('PRESIDENT','LEADER')")
    public List<ExpenseDto> getExpenses(@PathVariable("eventId") Long eventId) {
        return expenseService.getByEventId(eventId);
    }

    // ---------- CREATE ----------
    @PostMapping("/{eventId}/expenses")
    @PreAuthorize("hasAnyAuthority('PRESIDENT','LEADER')")
    public ResponseEntity<ExpenseDto> createExpense(@PathVariable("eventId") Long eventId,
                                                    @RequestBody CreateExpenseRequest request) {
        ExpenseDto created = expenseService.create(eventId, request);
        return ResponseEntity.ok(created);
    }

    // ---------- UPDATE ----------
    @PutMapping("/{eventId}/expenses/{expenseId}")
    @PreAuthorize("hasAnyAuthority('PRESIDENT','LEADER')")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable("eventId") Long eventId,
                                                    @PathVariable("expenseId") Long expenseId,
                                                    @RequestBody UpdateExpenseRequest request) {
        ExpenseDto updated = expenseService.update(eventId, expenseId, request);
        return ResponseEntity.ok(updated);
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{eventId}/expenses/{expenseId}")
    @PreAuthorize("hasAnyAuthority('PRESIDENT','LEADER')")
    public ResponseEntity<Void> deleteExpense(@PathVariable("eventId") Long eventId,
                                              @PathVariable("expenseId") Long expenseId) {
        expenseService.delete(eventId, expenseId);
        return ResponseEntity.noContent().build();
    }
}
