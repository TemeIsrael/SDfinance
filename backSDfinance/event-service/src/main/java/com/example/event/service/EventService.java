package com.example.event.service;

import com.example.common.model.UserRole;
import com.example.common.security.SecurityContextHelper;
import com.example.event.dto.CreateEventRequest;
import com.example.event.dto.EventDto;
import com.example.event.dto.EventSummaryDto;
import com.example.event.dto.UpdateEventRequest;
import com.example.event.model.Event;
import com.example.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository repository;
    private final SecurityContextHelper securityHelper;

    public List<EventDto> getAll() {
        UserRole role = securityHelper.getCurrentRole();
        if (role == UserRole.ADMIN || role == UserRole.MEMBRE) {
            throw new AccessDeniedException("Accès refusé aux événements");
        }
        
        List<Event> events;
        if (role == UserRole.LEADER) {
            events = repository.findAll(org.springframework.data.domain.Sort.by("startDate").descending());
        } else {
            events = repository.findByGroupeIdIn(securityHelper.getCurrentGroupIds());
        }
        
        return events.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<EventSummaryDto> getSummary() {
        securityHelper.requireRole(UserRole.LEADER);
        return repository.getEventSummaryByGroupe();
    }

    public EventDto getById(Long id) {
        Event event = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
        securityHelper.requireGroupeAccess(event.getGroupeId());
        return toDto(event);
    }

    @Transactional
    public EventDto create(CreateEventRequest req) {
        securityHelper.requireRole(UserRole.PRESIDENT);
        securityHelper.requireGroupeAccess(req.getGroupeId());

        Event event = Event.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .startDate(req.getStartDate())
                .location(req.getLocation())
                .groupeId(req.getGroupeId())
                .build();
        return toDto(repository.save(event));
    }

    @Transactional
    public EventDto update(Long id, UpdateEventRequest req) {
        securityHelper.requireRole(UserRole.PRESIDENT);
        Event event = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
        securityHelper.requireGroupeAccess(event.getGroupeId());

        event.setTitle(req.getTitle());
        event.setDescription(req.getDescription());
        event.setStartDate(req.getStartDate());
        event.setLocation(req.getLocation());
        return toDto(repository.save(event));
    }

    public void delete(Long id) {
        securityHelper.requireRole(UserRole.PRESIDENT);
        Event event = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
        securityHelper.requireGroupeAccess(event.getGroupeId());
        
        repository.deleteById(id);
    }

    private EventDto toDto(Event e) {
        return EventDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .startDate(e.getStartDate())
                .location(e.getLocation())
                .groupeId(e.getGroupeId())
                .build();
    }
}
