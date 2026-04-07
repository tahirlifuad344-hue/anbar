package com.school.warehouse.repository;

import com.school.warehouse.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByActiveTrue();
    Optional<Room> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT r FROM Room r LEFT JOIN FETCH r.items WHERE r.id = :id")
    Optional<Room> findByIdWithItems(Long id);
}