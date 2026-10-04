package com.example.messaging.controller;

import com.example.messaging.dto.CreateDiscussionRequest;
import com.example.messaging.dto.DiscussionDto;
import com.example.messaging.dto.MessageDto;
import com.example.messaging.model.Discussion;
import com.example.messaging.model.Message;
import com.example.messaging.repository.DiscussionRepository;
import com.example.messaging.repository.MessageRepository;
import com.example.messaging.service.DiscussionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messaging/discussions")
@CrossOrigin(origins = "*")
public class DiscussionController {

    private final DiscussionService discussionService;
    private final DiscussionRepository discussionRepository;
    private final MessageRepository messageRepository;

    public DiscussionController(DiscussionService discussionService,
                               DiscussionRepository discussionRepository,
                               MessageRepository messageRepository) {
        this.discussionService = discussionService;
        this.discussionRepository = discussionRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public ResponseEntity<List<DiscussionDto>> getDiscussions() {
        Optional<Discussion> globalOpt = discussionRepository.findByIsGlobalTrue();
        if (globalOpt.isPresent()) {
            Discussion d = globalOpt.get();
            DiscussionDto dto = DiscussionDto.builder()
                    .id(d.getId())
                    .ownerUsername(d.getOwnerUsername())
                    .counterpartUsername(d.getCounterpartUsername())
                    .lastMessagePreview(d.getLastMessagePreview())
                    .messageCount(d.getMessages().size())
                    .build();
            return ResponseEntity.ok(Collections.singletonList(dto));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageDto>> getMessages(@PathVariable Long id) {
        List<Message> messages = messageRepository.findByDiscussionIdOrderBySentAtAsc(id);
        List<MessageDto> dtos = messages.stream()
                .map(m -> MessageDto.builder()
                        .id(m.getId())
                        .senderUsername(m.getSenderUsername())
                        .content(m.getContent())
                        .sentAt(m.getSentAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<DiscussionDto> createDiscussion(@RequestBody CreateDiscussionRequest request) {
        // Ensure the global discussion exists and return it
        Discussion global = discussionService.ensureGlobalDiscussion();
        DiscussionDto dto = DiscussionDto.builder()
                .id(global.getId())
                .ownerUsername(global.getOwnerUsername())
                .counterpartUsername(global.getCounterpartUsername())
                .lastMessagePreview(global.getLastMessagePreview())
                .messageCount(global.getMessages().size())
                .build();
        return ResponseEntity.ok(dto);
    }
}
