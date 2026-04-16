package com.school.warehouse.controller;

import com.school.warehouse.common.response.ApiResponse;
import com.school.warehouse.dto.request.LoginRequest;
import com.school.warehouse.dto.response.AuthResponse;
import com.school.warehouse.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok("Uğurla daxil oldunuz", authService.login(request));
    }
}