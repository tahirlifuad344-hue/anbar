package com.school.warehouse.repository;

import com.school.warehouse.entity.InventoryItem;
import com.school.warehouse.entity.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByRoomId(Long roomId);
    List<InventoryItem> findByAssignedToId(Long userId);
    List<InventoryItem> findByStatus(ItemStatus status);
    Optional<InventoryItem> findByInventoryCode(String code);
    boolean existsByInventoryCode(String code);

    @Query("SELECT COUNT(i) FROM InventoryItem i")
    long countAllItems();

    @Query("SELECT i FROM InventoryItem i " +
            "LEFT JOIN FETCH i.room " +
            "LEFT JOIN FETCH i.assignedTo " +
            "WHERE (:roomId IS NULL OR i.room.id = :roomId) " +
            "AND (:status IS NULL OR i.status = :status)")
    List<InventoryItem> findWithFilters(
            @Param("roomId") Long roomId,
            @Param("status") ItemStatus status);
}