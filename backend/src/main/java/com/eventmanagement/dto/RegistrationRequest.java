package com.eventmanagement.dto;

import com.eventmanagement.validation.ValidPersonalCode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload for registering a participant to an event")
public record RegistrationRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name must not exceed 100 characters")
        @Schema(description = "Participant's first name", example = "Mari")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100, message = "Last name must not exceed 100 characters")
        @Schema(description = "Participant's last name", example = "Tamm")
        String lastName,

        @NotBlank(message = "Personal code is required")
        @ValidPersonalCode
        @Schema(description = "Estonian personal identification code", example = "49403136515")
        String personalCode
) {
}
