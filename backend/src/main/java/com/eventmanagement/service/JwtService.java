package com.eventmanagement.service;

import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.eventmanagement.config.AdminProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(AdminProperties properties) {
        this.signingKey = Keys.hmacShaKeyFor(properties.jwt().secret().getBytes());
        this.expirationMs = properties.jwt().expirationMs();
    }

    public String generateToken(String subject) {
        Date now = new Date();
        return Jwts.builder()
                .subject(subject)
                .claim("role", "ADMIN")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(signingKey)
                .compact();
    }

    public Optional<String> extractSubject(String token) {
        return parseToken(token)
                .map(Claims::getSubject);
    }

    public boolean isTokenValid(String token) {
        return parseToken(token).isPresent();
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    private Optional<Claims> parseToken(String token) {
        try {
            return Optional.of(
                    Jwts.parser()
                            .verifyWith(signingKey)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload()
            );
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
