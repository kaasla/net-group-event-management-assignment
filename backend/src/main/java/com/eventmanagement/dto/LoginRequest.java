package com.eventmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Admin login credentials")
public record LoginRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Schema(description = "Admin email address", example = "admin@eventmanagement.com")
        String email,

        @NotBlank(message = "Password is required")
        @Schema(description = "Admin password", example = "admin123")
        String password
) {
}
