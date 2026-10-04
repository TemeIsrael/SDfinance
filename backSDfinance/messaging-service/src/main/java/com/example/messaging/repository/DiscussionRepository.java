package com.example.messaging.repository;

import com.example.messaging.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Discussion entities.
 * Provides methods to retrieve the global discussion and user-specific discussions.
 */
@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    /**
     * Retrieves the single global discussion if it exists.
     */
    Optional<Discussion> findByIsGlobalTrue();

    /**
     * Retrieves discussions where the given user is either the owner or the counterpart.
     */
    List<Discussion> findByOwnerUsernameOrCounterpartUsername(String owner, String counterpart);
}
