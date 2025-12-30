package com.example.demo.auth.api;

import com.example.demo.auth.api.dto.LoginRequestDto;
import com.example.demo.auth.api.dto.LoginResponseDto;
import com.example.demo.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
        String token = authService.login(dto.email(), dto.password());
        return new LoginResponseDto(token);
    }
}
