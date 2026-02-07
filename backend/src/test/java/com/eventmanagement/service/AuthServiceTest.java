package com.eventmanagement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eventmanagement.config.AdminProperties;
import com.eventmanagement.dto.LoginRequest;
import com.eventmanagement.dto.LoginResponse;
import com.eventmanagement.exception.InvalidCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AdminProperties adminProperties;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginShouldReturnTokenForValidCredentials() {
        given(adminProperties.admin())
                .willReturn(new AdminProperties.Admin("admin@test.com", "hashed"));
        given(passwordEncoder.matches("password", "hashed")).willReturn(true);
        given(jwtService.generateToken("admin@test.com")).willReturn("jwt-token");
        given(jwtService.getExpirationMs()).willReturn(3600000L);

        LoginResponse response = authService.login(
                new LoginRequest("admin@test.com", "password"));

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresIn()).isEqualTo(3600000L);
    }

    @Test
    void loginShouldThrowWhenPasswordIsWrong() {
        given(adminProperties.admin())
                .willReturn(new AdminProperties.Admin("admin@test.com", "hashed"));
        given(passwordEncoder.matches("wrong", "hashed")).willReturn(false);

        assertThatThrownBy(() -> authService.login(
                new LoginRequest("admin@test.com", "wrong")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void loginShouldThrowWhenEmailIsWrong() {
        given(adminProperties.admin())
                .willReturn(new AdminProperties.Admin("admin@test.com", "hashed"));

        assertThatThrownBy(() -> authService.login(
                new LoginRequest("wrong@test.com", "password")))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
