package com.eventmanagement.service;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.eventmanagement.config.AdminProperties;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET =
            "a5f8d2e1b4c7a0e3f6d9b2c5a8e1d4f7b0c3a6e9d2f5b8c1a4e7d0f3b6c9a2e5";
    private static final long EXPIRATION_MS = 3600000L;
    private static final String SUBJECT = "admin@eventmanagement.com";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        AdminProperties properties = new AdminProperties(
                new AdminProperties.Admin("admin@test.com", "hashed"),
                new AdminProperties.Jwt(SECRET, EXPIRATION_MS)
        );
        jwtService = new JwtService(properties);
    }

    @Test
    void generateTokenShouldReturnNonNullToken() {
        String token = jwtService.generateToken(SUBJECT);
        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void extractSubjectShouldReturnCorrectEmail() {
        String token = jwtService.generateToken(SUBJECT);
        Optional<String> subject = jwtService.extractSubject(token);
        assertThat(subject).contains(SUBJECT);
    }

    @Test
    void isTokenValidShouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(SUBJECT);
        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void isTokenValidShouldReturnFalseForTamperedToken() {
        String token = jwtService.generateToken(SUBJECT);
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";
        assertThat(jwtService.isTokenValid(tampered)).isFalse();
    }

    @Test
    void extractSubjectShouldReturnEmptyForGarbage() {
        assertThat(jwtService.extractSubject("not.a.token")).isEmpty();
    }

    @Test
    void extractSubjectShouldReturnEmptyForNull() {
        assertThat(jwtService.extractSubject(null)).isEmpty();
    }

    @Test
    void isTokenValidShouldReturnFalseForExpiredToken() {
        AdminProperties expiredProps = new AdminProperties(
                new AdminProperties.Admin("admin@test.com", "hashed"),
                new AdminProperties.Jwt(SECRET, -1000L)
        );
        JwtService expiredService = new JwtService(expiredProps);
        String token = expiredService.generateToken(SUBJECT);
        assertThat(jwtService.isTokenValid(token)).isFalse();
    }

    @Test
    void getExpirationMsShouldReturnConfiguredValue() {
        assertThat(jwtService.getExpirationMs()).isEqualTo(EXPIRATION_MS);
    }
}
