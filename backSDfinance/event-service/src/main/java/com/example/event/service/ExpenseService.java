package com.example.event.service;

import com.example.event.dto.*;
import com.example.event.model.*;
import com.example.event.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service handling CRUD operations for {@link Expense} linked to an {@link Event}.
 */
@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final EventRepository eventRepository; // already exists in the project

    /**
     * Return all expenses for a given event.
     */
    public List<ExpenseDto> getByEventId(Long eventId) {
        List<Expense> expenses = expenseRepository.findByEventId(eventId);
        return expenses.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Create a new expense for the specified event.
     */
    @Transactional
    public ExpenseDto create(Long eventId, CreateExpenseRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        Expense expense = Expense.builder()
                .event(event)
                .description(request.getDescription())
                .amount(request.getAmount())
                .date(request.getDate())
                .build();
        expense = expenseRepository.save(expense);
        return toDto(expense);
    }

    /**
     * Update an existing expense belonging to the given event.
     */
    @Transactional
    public ExpenseDto update(Long eventId, Long expenseId, UpdateExpenseRequest request) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        if (!expense.getEvent().getId().equals(eventId)) {
            throw new IllegalArgumentException("Expense does not belong to the specified event");
        }
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense = expenseRepository.save(expense);
        return toDto(expense);
    }

    /**
     * Delete an expense.
     */
    @Transactional
    public void delete(Long eventId, Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        if (!expense.getEvent().getId().equals(eventId)) {
            throw new IllegalArgumentException("Expense does not belong to the specified event");
        }
        expenseRepository.delete(expense);
    }

    private ExpenseDto toDto(Expense e) {
        ExpenseDto dto = new ExpenseDto();
        dto.setId(e.getId());
        dto.setDescription(e.getDescription());
        dto.setAmount(e.getAmount());
        dto.setDate(e.getDate());
        return dto;
    }
}
