package com.eventmanagement.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eventmanagement.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final List<SimpleGrantedAuthority> ADMIN_AUTHORITIES =
            List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        extractBearerToken(request)
                .flatMap(jwtService::extractSubject)
                .ifPresent(subject -> setAuthentication(subject, request));

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractBearerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()));
    }

    private void setAuthentication(String subject, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(subject, null, ADMIN_AUTHORITIES);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
