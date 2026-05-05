package com.carrental.carrentalsystem.service;

import com.carrental.carrentalsystem.dto.AuthResponse;
import com.carrental.carrentalsystem.dto.LoginRequest;
import com.carrental.carrentalsystem.dto.RegisterRequest;
import com.carrental.carrentalsystem.entity.User;
import com.carrental.carrentalsystem.exception.BadRequestException;
import com.carrental.carrentalsystem.repository.UserRepository;
import com.carrental.carrentalsystem.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        User user = userRepository.save(User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build());

        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid login credentials"));

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        return AuthResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .tokenType("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
