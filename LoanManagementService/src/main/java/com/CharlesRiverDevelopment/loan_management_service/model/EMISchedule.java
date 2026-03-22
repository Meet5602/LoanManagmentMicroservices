package com.CharlesRiverDevelopment.loan_management_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EMISchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long loanId;

    private Integer installmentNumber;

    private BigDecimal emiAmount;

    private LocalDate dueDate;

    private BigDecimal principalComponent;

    private Boolean paid;

    private LocalDate paymentDate;
}
