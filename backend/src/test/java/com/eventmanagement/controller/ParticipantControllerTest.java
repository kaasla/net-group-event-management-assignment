package com.eventmanagement.controller;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eventmanagement.dto.ParticipantResponse;
import com.eventmanagement.dto.RegistrationRequest;
import com.eventmanagement.exception.DuplicateRegistrationException;
import com.eventmanagement.exception.EventFullException;
import com.eventmanagement.exception.ResourceNotFoundException;
import com.eventmanagement.service.ParticipantService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ParticipantControllerTest {

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private ParticipantController participantController;

    private static final Instant NOW = Instant.now();

    @Test
    void registerShouldReturnParticipant() {
        RegistrationRequest request = new RegistrationRequest("Mari", "Tamm", "49403136515");
        ParticipantResponse expected = new ParticipantResponse(1L, "Mari", "Tamm", "49403136515", NOW);
        given(participantService.registerParticipant(1L, request)).willReturn(expected);

        ParticipantResponse result = participantController.register(1L, request);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.firstName()).isEqualTo("Mari");
        assertThat(result.lastName()).isEqualTo("Tamm");
        assertThat(result.personalCode()).isEqualTo("49403136515");
    }

    @Test
    void registerShouldPropagateNotFoundForMissingEvent() {
        RegistrationRequest request = new RegistrationRequest("Mari", "Tamm", "49403136515");
        given(participantService.registerParticipant(999L, request))
                .willThrow(ResourceNotFoundException.event(999L));

        assertThatThrownBy(() -> participantController.register(999L, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void registerShouldPropagateDuplicateRegistration() {
        RegistrationRequest request = new RegistrationRequest("Mari", "Tamm", "49403136515");
        given(participantService.registerParticipant(1L, request))
                .willThrow(new DuplicateRegistrationException("49403136515", 1L));

        assertThatThrownBy(() -> participantController.register(1L, request))
                .isInstanceOf(DuplicateRegistrationException.class)
                .hasMessageContaining("49403136515");
    }

    @Test
    void registerShouldPropagateEventFull() {
        RegistrationRequest request = new RegistrationRequest("Mari", "Tamm", "49403136515");
        given(participantService.registerParticipant(1L, request))
                .willThrow(new EventFullException(1L));

        assertThatThrownBy(() -> participantController.register(1L, request))
                .isInstanceOf(EventFullException.class)
                .hasMessageContaining("maximum capacity");
    }

    @Test
    void getParticipantsShouldReturnList() {
        ParticipantResponse participant = new ParticipantResponse(1L, "Mari", "Tamm", "49403136515", NOW);
        given(participantService.getParticipantsByEventId(1L)).willReturn(List.of(participant));

        List<ParticipantResponse> result = participantController.getParticipants(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().firstName()).isEqualTo("Mari");
    }

    @Test
    void getParticipantsShouldPropagateNotFoundForMissingEvent() {
        given(participantService.getParticipantsByEventId(999L))
                .willThrow(ResourceNotFoundException.event(999L));

        assertThatThrownBy(() -> participantController.getParticipants(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
