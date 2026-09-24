package com.CharlesRiverDevlopement.payment_service.DTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {

    @NotNull
    private Long loanId;

    @NotNull
    private Long emiScheduleId;

    @NotNull
    @Positive
    private Double amount;
}