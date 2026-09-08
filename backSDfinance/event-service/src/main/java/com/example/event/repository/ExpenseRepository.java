package com.example.event.repository;

import com.example.event.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for {@link Expense} entities.
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByEventId(Long eventId);
}
