package com.example.demo.security;

import com.example.demo.auth.domain.User;
import com.example.demo.auth.repository.UserRepository;
import com.example.demo.exceptions.ReferenceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.annotation.Nonnull;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Nonnull
    public UserPrincipal loadUserByUsername(@Nonnull String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email));

        return UserPrincipal.from(user);
    }

    public UserPrincipal loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ReferenceNotFoundException("User not found with id: " + id));

        return UserPrincipal.from(user);
    }
}
