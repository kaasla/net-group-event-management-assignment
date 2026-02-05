package com.eventmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventmanagement.dto.CreateEventRequest;
import com.eventmanagement.dto.EventResponse;
import com.eventmanagement.entity.Event;
import com.eventmanagement.exception.ResourceNotFoundException;
import com.eventmanagement.mapper.EventMapper;
import com.eventmanagement.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAllByOrderByDateTimeAsc()
                .stream()
                .map(EventMapper::toResponse)
                .toList();
    }

    public EventResponse getEventById(Long id) {
        return eventRepository.findById(id)
                .map(EventMapper::toResponse)
                .orElseThrow(() -> ResourceNotFoundException.event(id));
    }

    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {
        Event event = Event.builder()
                .name(request.name())
                .dateTime(request.dateTime())
                .maxParticipants(request.maxParticipants())
                .build();

        Event savedEvent = eventRepository.save(event);
        return EventMapper.toResponse(savedEvent);
    }
}
