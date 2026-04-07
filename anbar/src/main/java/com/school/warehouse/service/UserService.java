package com.school.warehouse.service;

import com.school.warehouse.common.exception.*;
import com.school.warehouse.dto.request.CreateUserRequest;
import com.school.warehouse.dto.response.UserResponse;
import com.school.warehouse.entity.*;
import com.school.warehouse.repository.UserRepository;
import com.school.warehouse.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Bu istifadəçi adı artıq mövcuddur");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Bu email artıq istifadə olunur");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .role(request.getRole())
                .build();

        userRepository.save(user);

        auditLogService.log(SecurityUtils.getCurrentUsername(),
                ActionType.USER_CREATED,
                "Yeni istifadəçi yaradıldı: " + user.getUsername(),
                "User", user.getId(), null, user.getUsername());

        return toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public void deactivateUser(Long id) {
        User user = findById(id);
        user.setActive(false);
        userRepository.save(user);

        auditLogService.log(SecurityUtils.getCurrentUsername(),
                ActionType.USER_UPDATED,
                "İstifadəçi deaktiv edildi: " + user.getUsername(),
                "User", user.getId(), "active=true", "active=false");
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("İstifadəçi tapılmadı: " + id));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}