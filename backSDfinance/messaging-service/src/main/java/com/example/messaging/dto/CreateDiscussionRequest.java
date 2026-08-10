package com.example.messaging.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDiscussionRequest {
    private String ownerUsername;
    private String counterpartUsername;
}
