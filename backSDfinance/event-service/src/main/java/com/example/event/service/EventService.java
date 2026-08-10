package com.example.event.service;

import com.example.event.dto.CreateEventRequest;
import com.example.event.dto.EventDto;
import com.example.event.dto.UpdateEventRequest;
import com.example.event.model.Event;
import com.example.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository repository;

    public List<EventDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public EventDto getById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
    }

    @Transactional
    public EventDto create(CreateEventRequest req) {
        Event event = Event.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .startDate(req.getStartDate())
                .location(req.getLocation())
                .build();
        return toDto(repository.save(event));
    }

    @Transactional
    public EventDto update(Long id, UpdateEventRequest req) {
        Event event = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
        event.setTitle(req.getTitle());
        event.setDescription(req.getDescription());
        event.setStartDate(req.getStartDate());
        event.setLocation(req.getLocation());
        return toDto(repository.save(event));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Event not found: " + id);
        }
        repository.deleteById(id);
    }

    private EventDto toDto(Event e) {
        return EventDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .startDate(e.getStartDate())
                .location(e.getLocation())
                .build();
    }
}
