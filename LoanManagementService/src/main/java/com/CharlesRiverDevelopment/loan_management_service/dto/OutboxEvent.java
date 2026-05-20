package com.CharlesRiverDevelopment.loan_management_service.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String eventType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    private String payloadClass;

    @Enumerated(EnumType.STRING)
    private OutboxEventStatus status;

    private Integer retryCount;

    private LocalDateTime createdAt;

    private LocalDateTime publishedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = OutboxEventStatus.PENDING;
        }

        if (retryCount == null) {
            retryCount = 0;
        }
    }

}
