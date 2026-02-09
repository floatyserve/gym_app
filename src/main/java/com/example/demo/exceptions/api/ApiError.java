package com.example.demo.exceptions.api;

import java.util.Map;

public record ApiError(
        String code,
        String message,
        Map<String, Object> context
) {}
