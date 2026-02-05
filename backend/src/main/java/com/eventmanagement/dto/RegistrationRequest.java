package com.eventmanagement.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for registering a participant to an event.
 */
public record RegistrationRequest(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Personal code is required")
        String personalCode
) {
}
