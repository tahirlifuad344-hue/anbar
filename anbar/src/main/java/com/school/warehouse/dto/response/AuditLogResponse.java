package com.school.warehouse.dto.response;

import com.school.warehouse.entity.ActionType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AuditLogResponse {
    private Long id;
    private Long userId;
    private String username;
    private ActionType action;
    private String description;
    private String entityType;
    private Long entityId;
    private String oldValue;
    private String newValue;
    private LocalDateTime createdAt;
}