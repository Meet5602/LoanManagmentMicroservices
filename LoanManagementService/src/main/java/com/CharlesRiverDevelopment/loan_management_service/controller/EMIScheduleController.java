package com.CharlesRiverDevelopment.loan_management_service.controller;

import com.CharlesRiverDevelopment.loan_management_service.model.EMISchedule;
import com.CharlesRiverDevelopment.loan_management_service.service.EMIScheduleService;
import com.CharlesRiverDevlopement.DTOs.EMIDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

        return emiScheduleService.getEMIDetails(emiScheduleId);
    }
}
