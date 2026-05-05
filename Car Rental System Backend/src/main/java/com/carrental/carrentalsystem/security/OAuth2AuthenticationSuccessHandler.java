package com.carrental.carrentalsystem.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.carrental.carrentalsystem.dto.AuthResponse;
import com.carrental.carrentalsystem.entity.User;
import com.carrental.carrentalsystem.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final ObjectMapper objectMapper;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                        HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
                String email = authentication.getName();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ServletException("OAuth2 user could not be resolved"));

                String token = jwtService.generateToken(user);
                AuthResponse authResponse = AuthResponse.builder()
                                .accessToken(token)
                                .tokenType("Bearer")
                                .userId(user.getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build();

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(response.getOutputStream(), authResponse);
        }
}
