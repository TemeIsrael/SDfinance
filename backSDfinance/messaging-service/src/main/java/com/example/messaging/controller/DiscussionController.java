package com.example.messaging.controller;

import com.example.messaging.dto.CreateDiscussionRequest;
import com.example.messaging.dto.DiscussionDto;
import com.example.messaging.dto.MessageDto;
import com.example.messaging.model.Discussion;
import com.example.messaging.model.Message;
import com.example.messaging.repository.DiscussionRepository;
import com.example.messaging.repository.MessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messaging/discussions")
@CrossOrigin(origins = "*")
public class DiscussionController {

    private final DiscussionRepository discussionRepository;
    private final MessageRepository messageRepository;

    public DiscussionController(DiscussionRepository discussionRepository, MessageRepository messageRepository) {
        this.discussionRepository = discussionRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public ResponseEntity<List<DiscussionDto>> getDiscussions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Discussion> discussions = discussionRepository.findByOwnerUsernameOrCounterpartUsername(username, username);
        
        List<DiscussionDto> dtos = discussions.stream().map(d -> {
            // Determine display counterpart name relative to the current user
            String counterpart = d.getOwnerUsername().equals(username) ? d.getCounterpartUsername() : d.getOwnerUsername();
            return DiscussionDto.builder()
                    .id(d.getId())
                    .ownerUsername(d.getOwnerUsername())
                    .counterpartUsername(counterpart)
                    .lastMessagePreview(d.getLastMessagePreview())
                    .messageCount(d.getMessages().size())
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageDto>> getMessages(@PathVariable Long id) {
        List<Message> messages = messageRepository.findByDiscussionIdOrderBySentAtAsc(id);
        List<MessageDto> dtos = messages.stream().map(m -> MessageDto.builder()
                .id(m.getId())
                .senderUsername(m.getSenderUsername())
                .content(m.getContent())
                .sentAt(m.getSentAt())
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<DiscussionDto> createDiscussion(@RequestBody CreateDiscussionRequest request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // Find if discussion already exists
        List<Discussion> existing = discussionRepository.findByOwnerUsernameOrCounterpartUsername(currentUser, currentUser);
        for (Discussion d : existing) {
            if ((d.getOwnerUsername().equals(currentUser) && d.getCounterpartUsername().equals(request.getCounterpartUsername())) ||
                (d.getCounterpartUsername().equals(currentUser) && d.getOwnerUsername().equals(request.getCounterpartUsername()))) {
                
                DiscussionDto dto = DiscussionDto.builder()
                        .id(d.getId())
                        .ownerUsername(d.getOwnerUsername())
                        .counterpartUsername(request.getCounterpartUsername())
                        .lastMessagePreview(d.getLastMessagePreview())
                        .messageCount(d.getMessages().size())
                        .build();
                return ResponseEntity.ok(dto);
            }
        }

        Discussion discussion = Discussion.builder()
                .ownerUsername(currentUser)
                .counterpartUsername(request.getCounterpartUsername())
                .lastMessagePreview("")
                .build();

        Discussion saved = discussionRepository.save(discussion);
        DiscussionDto dto = DiscussionDto.builder()
                .id(saved.getId())
                .ownerUsername(saved.getOwnerUsername())
                .counterpartUsername(saved.getCounterpartUsername())
                .lastMessagePreview(saved.getLastMessagePreview())
                .messageCount(0)
                .build();
        
        return ResponseEntity.ok(dto);
    }
}
