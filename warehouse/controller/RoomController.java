package com.school.warehouse.controller;

import com.school.warehouse.common.response.ApiResponse;
import com.school.warehouse.dto.request.CreateRoomRequest;
import com.school.warehouse.dto.response.RoomResponse;
import com.school.warehouse.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ApiResponse<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        return ApiResponse.ok("Otaq yaradıldı", roomService.createRoom(request));
    }

    @GetMapping
    public ApiResponse<List<RoomResponse>> getAllRooms() {
        return ApiResponse.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ApiResponse<RoomResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(roomService.getById(id));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deactivate(@PathVariable Long id) {
        roomService.deactivateRoom(id);
        return ApiResponse.ok("Otaq deaktiv edildi", null);
    }
}