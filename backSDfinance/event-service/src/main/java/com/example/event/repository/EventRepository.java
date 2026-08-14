package com.example.event.repository;

import com.example.common.repository.GroupScopedRepository;
import com.example.event.model.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends GroupScopedRepository<Event, Long> {

    /**
     * Requête agrégée pour le LEADER : nombre d'événements par groupe.
     */
    @Query("SELECT new com.example.event.dto.EventSummaryDto(e.groupeId, COUNT(e)) " +
           "FROM Event e GROUP BY e.groupeId")
    List<com.example.event.dto.EventSummaryDto> getEventSummaryByGroupe();
}
