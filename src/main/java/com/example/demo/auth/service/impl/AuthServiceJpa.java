package com.example.demo.auth.service.impl;

import com.example.demo.auth.service.AuthService;
import com.example.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import com.example.demo.security.UserPrincipal;

@Service
@RequiredArgsConstructor
public class AuthServiceJpa implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(String email, String password) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (AuthenticationException ex) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        Long userId = ((UserPrincipal) authentication.getPrincipal()).getId();

        return jwtTokenProvider.generateToken(userId);
    }
}
