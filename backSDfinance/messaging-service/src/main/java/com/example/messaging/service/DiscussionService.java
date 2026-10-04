package com.example.messaging.service;

import com.example.messaging.model.Discussion;
import com.example.messaging.repository.DiscussionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiscussionService {

    private final DiscussionRepository discussionRepository;

    public DiscussionService(DiscussionRepository discussionRepository) {
        this.discussionRepository = discussionRepository;
    }

    /**
     * Ensure that a global discussion exists. If it does not, create it.
     * The global discussion has both ownerUsername and counterpartUsername set to "global"
     * and the flag isGlobal = true.
     */
    @Transactional
    public Discussion ensureGlobalDiscussion() {
        return discussionRepository.findByIsGlobalTrue().orElseGet(() -> {
            Discussion d = Discussion.builder()
                    .ownerUsername("global")
                    .counterpartUsername("global")
                    .isGlobal(true)
                    .lastMessagePreview("")
                    .build();
            return discussionRepository.save(d);
        });
    }
}
