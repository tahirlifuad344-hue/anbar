package com.school.warehouse.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoomRequest {
    @NotBlank(message = "Otaq adı boş ola bilməz")
    private String name;

    private String description;
}