package com.eventmanagement.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventmanagement.dto.ErrorResponse;
import com.eventmanagement.dto.LoginRequest;
import com.eventmanagement.dto.LoginResponse;
import com.eventmanagement.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "Admin authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Admin login", description = "Authenticate with admin credentials and receive a JWT token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request data",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
