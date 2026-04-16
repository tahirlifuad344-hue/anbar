package com.school.warehouse.service;

import com.school.warehouse.common.exception.*;
import com.school.warehouse.dto.request.*;
import com.school.warehouse.dto.response.ItemResponse;
import com.school.warehouse.entity.*;
import com.school.warehouse.repository.*;
import com.school.warehouse.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryItemRepository itemRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public ItemResponse createItem(CreateItemRequest request) {
        if (request.getInventoryCode() != null &&
                itemRepository.existsByInventoryCode(request.getInventoryCode())) {
            throw new BadRequestException("Bu inventar kodu artıq mövcuddur");
        }

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new NotFoundException("Otaq tapılmadı"));

        User assignedTo = null;
        if (request.getAssignedToUserId() != null) {
            assignedTo = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new NotFoundException("İstifadəçi tapılmadı"));
        }

        InventoryItem item = InventoryItem.builder()
                .name(request.getName())
                .inventoryCode(request.getInventoryCode())
                .description(request.getDescription())
                .quantity(request.getQuantity() != null ? request.getQuantity() : 1)
                .status(request.getStatus() != null ? request.getStatus() : ItemStatus.GOOD)
                .room(room)
                .assignedTo(assignedTo)
                .build();

        itemRepository.save(item);

        auditLogService.log(SecurityUtils.getCurrentUsername(),
                ActionType.ITEM_CREATED,
                "Yeni avadanlıq əlavə edildi: " + item.getName() + " → " + room.getName(),
                "InventoryItem", item.getId(), null, item.getName());

        return toResponse(item);
    }

    @Transactional
    public ItemResponse transferItem(TransferItemRequest request) {
        InventoryItem item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotFoundException("Avadanlıq tapılmadı"));

        Room targetRoom = roomRepository.findById(request.getTargetRoomId())
                .orElseThrow(() -> new NotFoundException("Hədəf otaq tapılmadı"));

        String oldRoomName = item.getRoom() != null ? item.getRoom().getName() : "yoxdur";
        String oldValue = "room=" + oldRoomName;
        String newValue = "room=" + targetRoom.getName();

        item.setRoom(targetRoom);
        itemRepository.save(item);

        String desc = String.format("'%s' avadanlığı '%s' otağından '%s' otağına köçürüldü. Səbəb: %s",
                item.getName(), oldRoomName, targetRoom.getName(),
                request.getReason() != null ? request.getReason() : "göstərilməyib");

        auditLogService.log(SecurityUtils.getCurrentUsername(),
                ActionType.ITEM_TRANSFERRED, desc,
                "InventoryItem", item.getId(), oldValue, newValue);

        return toResponse(item);
    }

    @Transactional
    public ItemResponse assignItem(Long itemId, Long userId) {
        InventoryItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Avadanlıq tapılmadı"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("İstifadəçi tapılmadı"));

        String oldValue = item.getAssignedTo() != null ?
                item.getAssignedTo().getUsername() : "heç kim";

        item.setAssignedTo(user);
        itemRepository.save(item);

        auditLogService.log(SecurityUtils.getCurrentUsername(),
                ActionType.ITEM_ASSIGNED,
                item.getName() + " avadanlığı " + user.getFullName() + "-a təyin edildi",
                "InventoryItem", item.getId(), oldValue, user.getUsername());

        return toResponse(item);
    }

    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<ItemResponse> getItemsByRoom(Long roomId) {
        return itemRepository.findByRoomId(roomId).stream().map(this::toResponse).toList();
    }

    public List<ItemResponse> getItemsByUser(Long userId) {
        return itemRepository.findByAssignedToId(userId).stream().map(this::toResponse).toList();
    }

    public ItemResponse getById(Long id) {
        return toResponse(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Avadanlıq tapılmadı")));
    }

    @Transactional
    public void deleteItem(Long id) {
        InventoryItem item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Avadanlıq tapılmadı"));

        auditLogService.log(SecurityUtils.getCurrentUsername(),
                ActionType.ITEM_DELETED,
                "Avadanlıq silindi: " + item.getName(),
                "InventoryItem", item.getId(), item.getName(), null);

        itemRepository.delete(item);
    }

    private ItemResponse toResponse(InventoryItem item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .inventoryCode(item.getInventoryCode())
                .description(item.getDescription())
                .quantity(item.getQuantity())
                .status(item.getStatus())
                .roomId(item.getRoom() != null ? item.getRoom().getId() : null)
                .roomName(item.getRoom() != null ? item.getRoom().getName() : null)
                .assignedToUserId(item.getAssignedTo() != null ? item.getAssignedTo().getId() : null)
                .assignedToName(item.getAssignedTo() != null ? item.getAssignedTo().getFullName() : null)
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
