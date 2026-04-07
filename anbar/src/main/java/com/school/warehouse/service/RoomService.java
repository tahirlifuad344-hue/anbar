package com.school.warehouse.service;

import com.school.warehouse.common.exception.*;
import com.school.warehouse.dto.request.CreateRoomRequest;
import com.school.warehouse.dto.response.RoomResponse;
import com.school.warehouse.entity.*;
import com.school.warehouse.repository.RoomRepository;
import com.school.warehouse.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public RoomResponse createRoom(CreateRoomRequest request) {
        if (roomRepository.existsByName(request.getName())) {
            throw new BadRequestException("Bu adda otaq artıq mövcuddur");
        }

        Room room = Room.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        roomRepository.save(room);

        auditLogService.log(SecurityUtils.getCurrentUsername(),
                ActionType.ROOM_CREATED,
                "Yeni otaq yaradıldı: " + room.getName(),
                "Room", room.getId(), null, room.getName());

        return toResponse(room);
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    public RoomResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public void deactivateRoom(Long id) {
        Room room = findById(id);
        room.setActive(false);
        roomRepository.save(room);

        auditLogService.log(SecurityUtils.getCurrentUsername(),
                ActionType.ROOM_UPDATED,
                "Otaq deaktiv edildi: " + room.getName(),
                "Room", room.getId(), "active=true", "active=false");
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Otaq tapılmadı: " + id));
    }

    private RoomResponse toResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .description(room.getDescription())
                .active(room.isActive())
                .itemCount(room.getItems().size())
                .build();
    }
}