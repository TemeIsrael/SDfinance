package com.example.messaging.repository;

import com.example.messaging.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    List<Discussion> findByOwnerUsernameOrCounterpartUsername(String owner, String counterpart);
}
