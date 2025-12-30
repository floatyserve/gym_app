package com.example.demo.auth.api;

import com.example.demo.auth.api.dto.CreateUserRequestDto;
import com.example.demo.auth.api.dto.UserResponseDto;
import com.example.demo.auth.domain.User;
import com.example.demo.auth.mapper.UserMapper;
import com.example.demo.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto create(@Valid @RequestBody CreateUserRequestDto dto) {
        User user = userService.create(dto.email(), dto.password(), dto.role());
        return userMapper.toDto(user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto getById(@PathVariable Long id) {
        return userMapper.toDto(userService.findById(id));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public void deactivate(@PathVariable Long id) {
        userService.deactivate(id);
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public void activate(@PathVariable Long id) {
        userService.activate(id);
    }
}
