package com.carrental.carrentalsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.carrental.carrentalsystem.dto.AuthResponse;
import com.carrental.carrentalsystem.dto.RegisterRequest;
import com.carrental.carrentalsystem.entity.User;
import com.carrental.carrentalsystem.enums.Role;
import com.carrental.carrentalsystem.exception.BadRequestException;
import com.carrental.carrentalsystem.repository.UserRepository;
import com.carrental.carrentalsystem.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("Alice");
        registerRequest.setEmail("alice@example.com");
        registerRequest.setPassword("secret123");
        registerRequest.setRole(Role.ROLE_USER);
    }

    @Test
    void registerShouldEncodePasswordAndReturnToken() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getEmail()).isEqualTo("alice@example.com");
        verify(passwordEncoder).encode("secret123");
    }

    @Test
    void registerShouldRejectDuplicateEmail() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already registered");
    }
}
