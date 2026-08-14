package com.example.finance.repository;

import com.example.common.repository.GroupScopedRepository;
import com.example.finance.model.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends GroupScopedRepository<Account, Long> {
    Optional<Account> findByIban(String iban);
    List<Account> findByOwnerUsername(String ownerUsername);
}
