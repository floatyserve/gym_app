package com.example.demo.auth.service;

public interface AuthService {
    String login(String email, String password);
    String changePassword(Long userId,
                          String oldPassword,
                          String newPassword);
}
