package com.CharlesRiverDevlopement.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EMIDetailsDTO {

    private Long emiScheduleId;

    private Long loanId;

    private Long userId;

    private Double emiAmount;

    private Boolean paid;
}