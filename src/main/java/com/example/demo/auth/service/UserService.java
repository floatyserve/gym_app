package com.example.demo.auth.service;

import com.example.demo.auth.domain.Role;
import com.example.demo.auth.domain.User;

public interface UserService {

    User findByEmail(String email);

    User findById(Long id);

    User create(String email, String rawPassword, Role role);

    void deactivate(Long currentUserId, Long targetUserId);

    void activate(Long userId);
}
