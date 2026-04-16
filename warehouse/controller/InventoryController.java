package com.school.warehouse.controller;

import com.school.warehouse.common.response.ApiResponse;
import com.school.warehouse.dto.request.*;
import com.school.warehouse.dto.response.ItemResponse;
import com.school.warehouse.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ApiResponse<ItemResponse> createItem(@Valid @RequestBody CreateItemRequest request) {
        return ApiResponse.ok("Avadanlıq əlavə edildi", inventoryService.createItem(request));
    }

    @GetMapping
    public ApiResponse<List<ItemResponse>> getAllItems() {
        return ApiResponse.ok(inventoryService.getAllItems());
    }

    @GetMapping("/{id}")
    public ApiResponse<ItemResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(inventoryService.getById(id));
    }

    @GetMapping("/room/{roomId}")
    public ApiResponse<List<ItemResponse>> getByRoom(@PathVariable Long roomId) {
        return ApiResponse.ok(inventoryService.getItemsByRoom(roomId));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ItemResponse>> getByUser(@PathVariable Long userId) {
        return ApiResponse.ok(inventoryService.getItemsByUser(userId));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ApiResponse<ItemResponse> transfer(@Valid @RequestBody TransferItemRequest request) {
        return ApiResponse.ok("Avadanlıq köçürüldü", inventoryService.transferItem(request));
    }

    @PostMapping("/{itemId}/assign/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ApiResponse<ItemResponse> assign(@PathVariable Long itemId,
                                            @PathVariable Long userId) {
        return ApiResponse.ok("Avadanlıq təyin edildi", inventoryService.assignItem(itemId, userId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        inventoryService.deleteItem(id);
        return ApiResponse.ok("Avadanlıq silindi", null);
    }
}