package com.school.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType action;

    @Column(length = 1000)
    private String description;

    @Column
    private String entityType; // "InventoryItem", "Room" və s.

    @Column
    private Long entityId;

    @Column
    private String oldValue; // dəyişiklikdən əvvəlki dəyər (JSON)

    @Column
    private String newValue; // dəyişiklikdən sonrakı dəyər (JSON)

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}