package com.school.warehouse.service;

import com.school.warehouse.common.exception.BadRequestException;
import com.school.warehouse.dto.request.LoginRequest;
import com.school.warehouse.dto.response.AuthResponse;
import com.school.warehouse.entity.*;
import com.school.warehouse.repository.UserRepository;
import com.school.warehouse.security.JwtService;
import com.school.warehouse.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadRequestException("İstifadəçi adı və ya şifrə yanlışdır");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("İstifadəçi tapılmadı"));

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());

        auditLogService.log(user.getUsername(), ActionType.LOGIN,
                user.getUsername() + " sistemə daxil oldu",
                "User", user.getId(), null, null);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
}