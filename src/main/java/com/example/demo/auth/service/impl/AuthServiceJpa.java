package com.example.demo.auth.service.impl;

import com.example.demo.auth.domain.User;
import com.example.demo.auth.service.AuthService;
import com.example.demo.auth.service.UserService;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.security.UserPrincipal;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceJpa implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(String email, String password) {
        Authentication authentication;

        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserPrincipal userPrincipal)) {
            throw new IllegalStateException(
                    "Authentication principal is not UserPrincipal"
            );
        }

        return jwtTokenProvider.generateToken(userPrincipal);
    }

    @Override
    public String changePassword(
            Long userId,
            String oldPassword,
            String newPassword
    ) {
        User user = userService.findById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid current password");
        }

        if (isSamePassword(user.getPasswordHash(), newPassword)) {
            throw new BadRequestException(
                    "New password must be different from the old one"
            );
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordChanged(true);

        return jwtTokenProvider.generateToken(UserPrincipal.from(user));
    }

    private boolean isSamePassword(String oldEncodedPassword, String newPassword) {
        return passwordEncoder.matches(newPassword, oldEncodedPassword);
    }

}
