package com.CharlesRiverDevelopment.loan_management_service.controller;

import com.CharlesRiverDevelopment.loan_management_service.model.EMISchedule;
import com.CharlesRiverDevelopment.loan_management_service.repository.EMIScheduleRepository;
import com.CharlesRiverDevelopment.loan_management_service.service.EMIScheduleService;
import com.CharlesRiverDevlopement.DTOs.EMIDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class EMIScheduleController {
    private final EMIScheduleService emiScheduleService;
    private final EMIScheduleRepository emiScheduleRepository;

    @GetMapping("/{loanId}/emi-schedule")
    public ResponseEntity<List<EMISchedule>> getEMISchedule(
            @PathVariable Long loanId) {

        return ResponseEntity.ok(
                emiScheduleService.getSchedule(loanId)
        );
    }

    @GetMapping("/emi-schedule/{emiScheduleId}")
    public EMIDetailsDTO getEMIDetails(
            @PathVariable Long emiScheduleId) {

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        EMISchedule emi = emiScheduleRepository.findById(emiScheduleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("EMI not found"));

        return EMIDetailsDTO.builder()
                .emiScheduleId(emi.getId())
                .loanId(emi.getLoanId())
                .userId(userId)
                .emiAmount(emi.getEmiAmount().doubleValue())
                .paid(emi.getPaid())
                .build();
    }
}
