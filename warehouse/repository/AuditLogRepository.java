package com.school.warehouse.repository;

import com.school.warehouse.entity.ActionType;
import com.school.warehouse.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByUserId(Long userId, Pageable pageable);
    Page<AuditLog> findByAction(ActionType action, Pageable pageable);
    List<AuditLog> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    Page<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}