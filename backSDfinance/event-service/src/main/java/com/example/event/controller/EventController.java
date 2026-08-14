package com.example.event.controller;

import com.example.event.dto.EventDto;
import com.example.event.dto.EventSummaryDto;
import com.example.event.dto.CreateEventRequest;
import com.example.event.dto.UpdateEventRequest;
import com.example.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('PRESIDENT', 'TRESORIER_CAISSIER', 'LEADER')")
    public List<EventDto> getAll() {
        return eventService.getAll();
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('LEADER')")
    public List<EventSummaryDto> getSummary() {
        return eventService.getSummary();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('PRESIDENT', 'TRESORIER_CAISSIER')")
    public EventDto getById(@PathVariable Long id) {
        return eventService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRESIDENT')")
    public ResponseEntity<EventDto> create(@RequestBody CreateEventRequest request) {
        EventDto created = eventService.create(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESIDENT')")
    public ResponseEntity<EventDto> update(@PathVariable Long id, @RequestBody UpdateEventRequest request) {
        EventDto updated = eventService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESIDENT')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
