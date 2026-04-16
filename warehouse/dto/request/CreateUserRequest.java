package com.school.warehouse.dto.request;

import com.school.warehouse.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "İstifadəçi adı boş ola bilməz")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, message = "Şifrə minimum 6 simvol olmalıdır")
    private String password;

    @NotBlank
    private String fullName;

    @Email(message = "Email düzgün formatda deyil")
    @NotBlank
    private String email;

    @NotNull
    private Role role;
}