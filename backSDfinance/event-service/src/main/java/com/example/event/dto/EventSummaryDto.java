package com.example.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la vue sommaire des événements par le LEADER.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSummaryDto {
    private Long groupeId;
    private Long nombreEvenements;
}
