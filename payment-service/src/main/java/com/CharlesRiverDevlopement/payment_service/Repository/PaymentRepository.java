package com.CharlesRiverDevlopement.payment_service.Repository;

import com.CharlesRiverDevlopement.payment_service.model.Payment;
import com.CharlesRiverDevlopement.payment_service.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Additional query methods can be defined here if needed
    Optional<Payment> findByEmiScheduleId(Long emiScheduleId);

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    boolean existsByEmiScheduleIdAndStatus(Long emiScheduleId, PaymentStatus paymentStatus);
}
