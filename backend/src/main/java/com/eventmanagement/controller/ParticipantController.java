package com.eventmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eventmanagement.dto.ErrorResponse;
import com.eventmanagement.dto.ParticipantResponse;
import com.eventmanagement.dto.RegistrationRequest;
import com.eventmanagement.service.ParticipantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/events/{eventId}/participants")
@RequiredArgsConstructor
@Validated
@Tag(name = "Participants", description = "Event registration endpoints")
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register for event", description = "Register a participant for the specified event")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Registration successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request data",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Event not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Event full or already registered",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ParticipantResponse register(
            @Parameter(description = "ID of the event to register for", example = "1")
            @PathVariable Long eventId,
            @Valid @RequestBody RegistrationRequest request
    ) {
        return participantService.registerParticipant(eventId, request);
    }

    @GetMapping
    @Operation(summary = "Get event participants", description = "Returns all participants for the specified event")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participants retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Event not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<ParticipantResponse> getParticipants(
            @Parameter(description = "ID of the event", example = "1")
            @PathVariable Long eventId
    ) {
        return participantService.getParticipantsByEventId(eventId);
    }
}
