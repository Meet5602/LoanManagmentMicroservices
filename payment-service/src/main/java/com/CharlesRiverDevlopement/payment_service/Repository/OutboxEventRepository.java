package com.CharlesRiverDevlopement.payment_service.Repository;

import com.CharlesRiverDevlopement.payment_service.DTO.OutboxEventStatus;
import com.CharlesRiverDevlopement.payment_service.outbox.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findTop10ByStatusOrderByCreatedAtAsc(
            OutboxEventStatus status
    );
}