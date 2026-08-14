package com.example.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface GroupScopedRepository<T, ID> extends JpaRepository<T, ID> {
    List<T> findByGroupeIdIn(List<Long> groupeIds);
    long countByGroupeIdIn(List<Long> groupeIds);
}
