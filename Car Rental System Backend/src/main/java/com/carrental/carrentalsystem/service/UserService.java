package com.carrental.carrentalsystem.service;

import com.carrental.carrentalsystem.dto.NotificationResponse;
import com.carrental.carrentalsystem.dto.UserResponse;
import com.carrental.carrentalsystem.entity.User;
import com.carrental.carrentalsystem.exception.ResourceNotFoundException;
import com.carrental.carrentalsystem.repository.NotificationRepository;
import com.carrental.carrentalsystem.repository.UserRepository;
import com.carrental.carrentalsystem.util.EntityDtoMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(EntityDtoMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        return EntityDtoMapper.toUserResponse(findEntityById(id));
    }

    public UserResponse getUserByEmail(String email) {
        return EntityDtoMapper.toUserResponse(findEntityByEmail(email));
    }

    public List<NotificationResponse> getNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(EntityDtoMapper::toNotificationResponse)
                .toList();
    }

    public User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
