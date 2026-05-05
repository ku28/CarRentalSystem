package com.carrental.carrentalsystem.controller;

import com.carrental.carrentalsystem.dto.NotificationResponse;
import com.carrental.carrentalsystem.dto.UserResponse;
import com.carrental.carrentalsystem.service.UserService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser(Principal principal) {
        return userService.getUserByEmail(principal.getName());
    }

    @GetMapping("/me/notifications")
    public List<NotificationResponse> getMyNotifications(Principal principal) {
        UserResponse user = userService.getUserByEmail(principal.getName());
        return userService.getNotifications(user.getId());
    }
}
