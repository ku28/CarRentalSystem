package com.carrental.carrentalsystem.security;

import com.carrental.carrentalsystem.entity.User;
import com.carrental.carrentalsystem.enums.Role;
import java.util.HashMap;
import com.carrental.carrentalsystem.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
        String login = String.valueOf(attributes.getOrDefault("login", "github-user"));
        String emailValue = (String) attributes.get("email");
        if (emailValue == null || emailValue.isBlank()) {
            emailValue = login + "@github.oauth.local";
        }
        String name = String.valueOf(attributes.getOrDefault("name", login));
        attributes.put("email", emailValue);
        final String email = emailValue;

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .name(name)
                        .email(email)
                        .password(passwordEncoder.encode("oauth2-user"))
                        .role(Role.ROLE_USER)
                        .build()));

        return new DefaultOAuth2User(
                java.util.List.of(() -> user.getRole().name()),
                attributes,
                "email"
        );
    }
}
