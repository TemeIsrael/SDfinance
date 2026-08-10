package com.example.messaging.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionDto {
    private Long id;
    private String ownerUsername;
    private String counterpartUsername;
    private String lastMessagePreview;
    private int messageCount;
}
