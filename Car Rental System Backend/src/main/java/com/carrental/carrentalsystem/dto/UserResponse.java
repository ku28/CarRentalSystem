package com.carrental.carrentalsystem.dto;

import com.carrental.carrentalsystem.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
}
