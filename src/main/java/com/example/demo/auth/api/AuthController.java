package com.example.demo.auth.api;

import com.example.demo.auth.api.dto.ChangePasswordRequestDto;
import com.example.demo.auth.api.dto.LoginRequestDto;
import com.example.demo.auth.api.dto.LoginResponseDto;
import com.example.demo.auth.service.AuthService;
import com.example.demo.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/change-password")
    public LoginResponseDto changePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChangePasswordRequestDto req
    ) throws BadRequestException {
        String token = authService.changePassword(
                principal.getId(),
                req.oldPassword(),
                req.newPassword()
        );

        return new LoginResponseDto(token);
    }
}
