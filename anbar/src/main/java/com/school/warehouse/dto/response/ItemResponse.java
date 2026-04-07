package com.school.warehouse.dto.response;

import com.school.warehouse.entity.ItemStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemResponse {
    private Long id;
    private String name;
    private String inventoryCode;
    private String description;
    private Integer quantity;
    private ItemStatus status;
    private Long roomId;
    private String roomName;
    private Long assignedToUserId;
    private String assignedToName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}