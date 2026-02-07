package com.eventmanagement.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eventmanagement.dto.LoginRequest;
import com.eventmanagement.dto.LoginResponse;
import com.eventmanagement.exception.InvalidCredentialsException;
import com.eventmanagement.service.AuthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginShouldReturnTokenForValidCredentials() {
        LoginRequest request = new LoginRequest("admin@eventmanagement.com", "admin123");
        LoginResponse expected = LoginResponse.of("jwt-token", 3600000L);
        given(authService.login(request)).willReturn(expected);

        LoginResponse result = authController.login(request);

        assertThat(result.token()).isEqualTo("jwt-token");
        assertThat(result.tokenType()).isEqualTo("Bearer");
        assertThat(result.expiresIn()).isEqualTo(3600000L);
    }

    @Test
    void loginShouldPropagateInvalidCredentialsException() {
        LoginRequest request = new LoginRequest("admin@eventmanagement.com", "wrong");
        given(authService.login(request)).willThrow(new InvalidCredentialsException());

        assertThatThrownBy(() -> authController.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
