package com.example.demo.auth.service.impl;

import com.example.demo.auth.domain.Role;
import com.example.demo.auth.domain.User;
import com.example.demo.auth.repository.UserRepository;
import com.example.demo.auth.service.UserService;
import com.example.demo.exceptions.ReferenceNotFoundException;
import com.example.demo.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceJpa implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ReferenceNotFoundException("User with email: " + email + " was not found"));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ReferenceNotFoundException("User with id: " + id + " was not found"));
    }

    @Override
    public User create(String email, String rawPassword, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }

        User user = new User(
                email,
                passwordEncoder.encode(rawPassword),
                role
        );

        return userRepository.save(user);
    }

    @Override
    public void deactivate(Long currentUserId, Long targetUserId) {
        if (currentUserId.equals(targetUserId)) {
            throw new BadRequestException("You cannot deactivate yourself");
        }

        User user = findById(targetUserId);
        user.deactivate();
    }

    @Override
    public void activate(Long currentUserId, Long targetUserId) {
        if (currentUserId.equals(targetUserId)) {
            throw new BadRequestException("You cannot activate yourself");
        }

        User user = findById(targetUserId);
        user.activate();
    }
}
