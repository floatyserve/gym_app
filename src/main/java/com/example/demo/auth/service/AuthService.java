package com.example.demo.auth.service;

import org.apache.coyote.BadRequestException;

public interface AuthService {
    String login(String email, String password);
    String changePassword(Long userId,
                          String oldPassword,
                          String newPassword) throws BadRequestException;
}
