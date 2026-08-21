package com.example.messaging.controller;

import com.example.messaging.dto.MessageDto;
import com.example.messaging.model.Discussion;
import com.example.messaging.model.Message;
import com.example.messaging.repository.DiscussionRepository;
import com.example.messaging.repository.MessageRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final DiscussionRepository discussionRepository;

    public ChatController(SimpMessagingTemplate messagingTemplate, 
                          MessageRepository messageRepository, 
                          DiscussionRepository discussionRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.discussionRepository = discussionRepository;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(MessageSendPayload payload, Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("Not authenticated");
        }
        
        String senderUsername = principal.getName();
        Discussion discussion = discussionRepository.findById(payload.getDiscussionId())
                .orElseThrow(() -> new IllegalArgumentException("Discussion not found"));

        Message message = Message.builder()
                .discussion(discussion)
                .senderUsername(senderUsername)
                .content(payload.getContent())
                .sentAt(LocalDateTime.now())
                .build();

        Message saved = messageRepository.save(message);

        // Update last message preview
        discussion.setLastMessagePreview(senderUsername + ": " + payload.getContent());
        discussionRepository.save(discussion);

        MessageDto response = MessageDto.builder()
                .id(saved.getId())
                .senderUsername(senderUsername)
                .content(saved.getContent())
                .sentAt(saved.getSentAt())
                .build();

        // Broadcast to the participants of this discussion.
        messagingTemplate.convertAndSend("/topic/discussion/" + discussion.getId(), response);
    }

    public static class MessageSendPayload {
        private Long discussionId;
        private String content;

        public Long getDiscussionId() {
            return discussionId;
        }

        public void setDiscussionId(Long discussionId) {
            this.discussionId = discussionId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
