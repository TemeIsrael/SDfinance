package com.example.event.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {
    private String title;
    private String description;
    private LocalDateTime startDate;
    private String location;
}
