package com.carrental.carrentalsystem.dto;

import com.carrental.carrentalsystem.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private Long userId;
    private String name;
    private String email;
    private Role role;
}
