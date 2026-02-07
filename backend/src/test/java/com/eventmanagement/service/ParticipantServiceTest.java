package com.eventmanagement.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eventmanagement.dto.ParticipantResponse;
import com.eventmanagement.dto.RegistrationRequest;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.Participant;
import com.eventmanagement.exception.DuplicateRegistrationException;
import com.eventmanagement.exception.EventFullException;
import com.eventmanagement.exception.ResourceNotFoundException;
import com.eventmanagement.repository.EventRepository;
import com.eventmanagement.repository.ParticipantRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private ParticipantService participantService;

    private static final RegistrationRequest VALID_REQUEST =
            new RegistrationRequest("Mari", "Tamm", "49403136515");

    @Test
    void registerParticipantShouldReturnMappedResponse() {
        Event event = Event.builder().id(1L).name("Conference")
                .dateTime(Instant.now()).maxParticipants(50).build();
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        given(participantRepository.existsByEventIdAndPersonalCode(1L, "49403136515"))
                .willReturn(false);
        given(participantRepository.save(any(Participant.class))).willAnswer(invocation -> {
            Participant p = invocation.getArgument(0);
            return Participant.builder()
                    .id(1L).event(p.getEvent())
                    .firstName(p.getFirstName()).lastName(p.getLastName())
                    .personalCode(p.getPersonalCode()).build();
        });

        ParticipantResponse result = participantService.registerParticipant(1L, VALID_REQUEST);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.firstName()).isEqualTo("Mari");
        assertThat(result.personalCode()).isEqualTo("49403136515");
    }

    @Test
    void registerParticipantShouldThrowWhenEventNotFound() {
        given(eventRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> participantService.registerParticipant(999L, VALID_REQUEST))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void registerParticipantShouldThrowWhenEventFull() {
        Event fullEvent = Event.builder().id(1L).name("Full Event")
                .dateTime(Instant.now()).maxParticipants(1)
                .participants(List.of(
                        Participant.builder().id(99L).firstName("Existing").lastName("User")
                                .personalCode("39912310174").build()
                ))
                .build();
        given(eventRepository.findById(1L)).willReturn(Optional.of(fullEvent));

        assertThatThrownBy(() -> participantService.registerParticipant(1L, VALID_REQUEST))
                .isInstanceOf(EventFullException.class)
                .hasMessageContaining("maximum capacity");
    }

    @Test
    void registerParticipantShouldThrowWhenDuplicate() {
        Event event = Event.builder().id(1L).name("Conference")
                .dateTime(Instant.now()).maxParticipants(50).build();
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        given(participantRepository.existsByEventIdAndPersonalCode(1L, "49403136515"))
                .willReturn(true);

        assertThatThrownBy(() -> participantService.registerParticipant(1L, VALID_REQUEST))
                .isInstanceOf(DuplicateRegistrationException.class)
                .hasMessageContaining("49403136515");
    }

    @Test
    void getParticipantsByEventIdShouldReturnMappedList() {
        Participant participant = Participant.builder()
                .id(1L).firstName("Mari").lastName("Tamm")
                .personalCode("49403136515").build();
        given(eventRepository.existsById(1L)).willReturn(true);
        given(participantRepository.findAllByEventIdOrderByCreatedAtAsc(1L))
                .willReturn(List.of(participant));

        List<ParticipantResponse> result = participantService.getParticipantsByEventId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().firstName()).isEqualTo("Mari");
    }

    @Test
    void getParticipantsByEventIdShouldThrowWhenEventNotFound() {
        given(eventRepository.existsById(999L)).willReturn(false);

        assertThatThrownBy(() -> participantService.getParticipantsByEventId(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
