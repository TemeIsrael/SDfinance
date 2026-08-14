package com.example.event.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", indexes = {
        @Index(name = "idx_event_groupe", columnList = "groupe_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2048)
    private String description;

    private LocalDateTime startDate;
    private String location;

    @Column(name = "groupe_id")
    private Long groupeId;
}
