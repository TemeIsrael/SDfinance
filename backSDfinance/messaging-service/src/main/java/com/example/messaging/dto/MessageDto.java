package com.example.messaging.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private String senderUsername;
    private String content;
    private LocalDateTime sentAt;
}
