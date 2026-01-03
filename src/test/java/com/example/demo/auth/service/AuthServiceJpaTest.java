package com.example.demo.auth.service;

import com.example.demo.auth.service.impl.AuthServiceJpa;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceJpaTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private UserPrincipal userPrincipal;

    private AuthServiceJpa authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceJpa(authenticationManager, jwtTokenProvider);
    }

    @Test
    void login_returnsJwtToken_whenCredentialsAreValid() {
        String email = "test@test.com";
        String password = "password";
        Long userId = 42L;
        String token = "jwt-token";

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        )).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getId()).thenReturn(userId);
        when(jwtTokenProvider.generateToken(userId)).thenReturn(token);

        String result = authService.login(email, password);

        assertThat(result).isEqualTo(token);
    }

    @Test
    void login_throwsIllegalArgumentException_whenCredentialsAreInvalid() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("bad credentials"));

        assertThatThrownBy(() -> authService.login("test@test.com", "wrong"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email or password");

        verifyNoInteractions(jwtTokenProvider);
    }
}
