package com.eventmanagement.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eventmanagement.dto.CreateEventRequest;
import com.eventmanagement.dto.EventResponse;
import com.eventmanagement.entity.Event;
import com.eventmanagement.exception.ResourceNotFoundException;
import com.eventmanagement.repository.EventRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void getAllEventsShouldReturnMappedList() {
        Event event = Event.builder().id(1L).name("Conference")
                .dateTime(Instant.now()).maxParticipants(50).build();
        given(eventRepository.findAllByOrderByDateTimeAsc()).willReturn(List.of(event));

        List<EventResponse> result = eventService.getAllEvents();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Conference");
    }

    @Test
    void getEventByIdShouldReturnMappedResponse() {
        Event event = Event.builder().id(1L).name("Conference")
                .dateTime(Instant.now()).maxParticipants(50).build();
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));

        EventResponse result = eventService.getEventById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Conference");
        assertThat(result.maxParticipants()).isEqualTo(50);
    }

    @Test
    void getEventByIdShouldThrowWhenNotFound() {
        given(eventRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getEventById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void createEventShouldReturnMappedResponse() {
        Instant dateTime = Instant.parse("2026-12-01T18:00:00Z");
        CreateEventRequest request = new CreateEventRequest("New Event", dateTime, 100);

        given(eventRepository.save(any(Event.class))).willAnswer(invocation -> {
            Event e = invocation.getArgument(0);
            return Event.builder()
                    .id(1L).name(e.getName())
                    .dateTime(e.getDateTime()).maxParticipants(e.getMaxParticipants())
                    .build();
        });

        EventResponse result = eventService.createEvent(request);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("New Event");
        assertThat(result.maxParticipants()).isEqualTo(100);
        assertThat(result.participantCount()).isZero();
        assertThat(result.availableSpots()).isEqualTo(100);
    }
}
