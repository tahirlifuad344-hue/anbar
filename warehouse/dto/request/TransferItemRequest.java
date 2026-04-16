package com.school.warehouse.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransferItemRequest {
    @NotNull(message = "Avadanlıq ID-si boş ola bilməz")
    private Long itemId;

    @NotNull(message = "Hədəf otaq ID-si boş ola bilməz")
    private Long targetRoomId;

    private String reason; // köçürmə səbəbi
}