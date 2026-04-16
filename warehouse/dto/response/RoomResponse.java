package com.school.warehouse.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomResponse {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private int itemCount;
}