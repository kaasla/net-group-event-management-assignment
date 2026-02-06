package com.eventmanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@ConfigurationProperties(prefix = "app")
public record AdminProperties(
        Admin admin,
        Jwt jwt
) {

    @Schema(hidden = true)
    public record Admin(
            String email,
            String password
    ) {
    }

    @Schema(hidden = true)
    public record Jwt(
            String secret,
            long expirationMs
    ) {
    }
}
