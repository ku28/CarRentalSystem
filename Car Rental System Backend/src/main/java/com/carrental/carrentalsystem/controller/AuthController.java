package com.carrental.carrentalsystem.controller;

import com.carrental.carrentalsystem.dto.AuthResponse;
import com.carrental.carrentalsystem.dto.LoginRequest;
import com.carrental.carrentalsystem.dto.MessageResponse;
import com.carrental.carrentalsystem.dto.RegisterRequest;
import com.carrental.carrentalsystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/oauth2/github")
    public MessageResponse githubLoginInfo() {
        return new MessageResponse("Open /oauth2/authorization/github in a browser to continue GitHub OAuth2 login.");
    }
}
