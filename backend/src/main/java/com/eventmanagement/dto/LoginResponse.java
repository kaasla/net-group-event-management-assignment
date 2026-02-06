package com.eventmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Successful authentication response containing a JWT token")
public record LoginResponse(
        @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzM4NCJ9...")
        String token,

        @Schema(description = "Token type", example = "Bearer")
        String tokenType,

        @Schema(description = "Token expiration time in milliseconds", example = "3600000")
        long expiresIn
) {

    public static LoginResponse of(String token, long expiresIn) {
        return new LoginResponse(token, "Bearer", expiresIn);
    }
}
