package com.carrental.carrentalsystem.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.carrental.carrentalsystem.entity.User;
import com.carrental.carrentalsystem.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        private final UserRepository userRepository;
        private final JwtService jwtService;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                        HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
                String email = authentication.getName();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ServletException("OAuth2 user could not be resolved"));

                String token = jwtService.generateToken(user);

                String redirectUrl = "http://100.53.218.250/oauth2/callback"
                                + "?token=" + token
                                + "&userId=" + user.getId()
                                + "&name=" + java.net.URLEncoder.encode(user.getName(), "UTF-8")
                                + "&email=" + java.net.URLEncoder.encode(user.getEmail(), "UTF-8")
                                + "&role=" + user.getRole();

                response.sendRedirect(redirectUrl);
        }
}
