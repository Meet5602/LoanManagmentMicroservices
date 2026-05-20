package com.CharlesRiverDevelopment.notification_service.Repository;

import com.CharlesRiverDevelopment.notification_service.models.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, UUID> {
    //for idempotency check if a notification for the same event has already been sent
    Optional<NotificationHistory> findByEventId(UUID eventId);
}
