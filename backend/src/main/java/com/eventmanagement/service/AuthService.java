package com.eventmanagement.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eventmanagement.config.AdminProperties;
import com.eventmanagement.dto.LoginRequest;
import com.eventmanagement.dto.LoginResponse;
import com.eventmanagement.exception.InvalidCredentialsException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminProperties adminProperties;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        if (!isValidCredentials(request)) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(request.email());
        return LoginResponse.of(token, jwtService.getExpirationMs());
    }

    private boolean isValidCredentials(LoginRequest request) {
        String configuredPassword = adminProperties.admin().password();
        boolean emailMatches = adminProperties.admin().email().equals(request.email());
        boolean passwordMatches = configuredPassword.startsWith("$2a$")
                ? passwordEncoder.matches(request.password(), configuredPassword)
                : request.password().equals(configuredPassword);
        return emailMatches && passwordMatches;
    }
}
