package com.example.messaging.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "discussions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discussion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who owns/starts the discussion
    private String ownerUsername;

    // The interlocutor's username – displayed as the conversation name
    private String counterpartUsername;

    // Preview of the latest message, updated on each new Message
    private String lastMessagePreview;

    @OneToMany(mappedBy = "discussion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();
}
