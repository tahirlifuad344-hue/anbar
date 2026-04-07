package com.school.warehouse.dto.response;

import com.school.warehouse.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private String fullName;
    private Role role;
}