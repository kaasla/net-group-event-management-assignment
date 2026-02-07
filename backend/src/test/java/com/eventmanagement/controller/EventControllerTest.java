package com.eventmanagement.controller;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eventmanagement.dto.CreateEventRequest;
import com.eventmanagement.dto.EventResponse;
import com.eventmanagement.exception.ResourceNotFoundException;
import com.eventmanagement.service.EventService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private static final Instant NOW = Instant.now();

    @Test
    void getAllEventsShouldReturnListFromService() {
        EventResponse event = new EventResponse(1L, "Conference", NOW, 100, 42, 58, NOW);
        given(eventService.getAllEvents()).willReturn(List.of(event));

        List<EventResponse> result = eventController.getAllEvents();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Conference");
    }

    @Test
    void getAllEventsShouldReturnEmptyListWhenNoEvents() {
        given(eventService.getAllEvents()).willReturn(List.of());

        List<EventResponse> result = eventController.getAllEvents();

        assertThat(result).isEmpty();
    }

    @Test
    void getEventByIdShouldReturnEvent() {
        EventResponse event = new EventResponse(1L, "Conference", NOW, 100, 42, 58, NOW);
        given(eventService.getEventById(1L)).willReturn(event);

        EventResponse result = eventController.getEventById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Conference");
        assertThat(result.participantCount()).isEqualTo(42);
        assertThat(result.availableSpots()).isEqualTo(58);
    }

    @Test
    void getEventByIdShouldPropagateNotFound() {
        given(eventService.getEventById(999L))
                .willThrow(ResourceNotFoundException.event(999L));

        assertThatThrownBy(() -> eventController.getEventById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void createEventShouldReturnCreatedEvent() {
        Instant dateTime = Instant.parse("2026-12-01T18:00:00Z");
        CreateEventRequest request = new CreateEventRequest("New Event", dateTime, 50);
        EventResponse expected = new EventResponse(1L, "New Event", dateTime, 50, 0, 50, NOW);
        given(eventService.createEvent(request)).willReturn(expected);

        EventResponse result = eventController.createEvent(request);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("New Event");
        assertThat(result.maxParticipants()).isEqualTo(50);
        assertThat(result.participantCount()).isZero();
        assertThat(result.availableSpots()).isEqualTo(50);
    }
}
