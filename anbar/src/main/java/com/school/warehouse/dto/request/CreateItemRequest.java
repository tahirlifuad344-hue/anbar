package com.school.warehouse.dto.request;

import com.school.warehouse.entity.ItemStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateItemRequest {
    @NotBlank(message = "Avadanlıq adı boş ola bilməz")
    private String name;

    private String inventoryCode;
    private String description;

    @Min(1)

    @Builder.Default
    private Integer quantity = 1;

    private ItemStatus status;

    @NotNull(message = "Otaq seçilməlidir")
    private Long roomId;

    private Long assignedToUserId;
}