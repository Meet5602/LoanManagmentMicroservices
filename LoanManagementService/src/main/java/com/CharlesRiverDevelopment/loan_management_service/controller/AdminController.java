package com.CharlesRiverDevelopment.loan_management_service.controller;

import com.CharlesRiverDevelopment.loan_management_service.model.KYC;
import com.CharlesRiverDevelopment.loan_management_service.model.LoanApplication;
import com.CharlesRiverDevelopment.loan_management_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired private AdminService service;

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public LoanApplication approve(@PathVariable Long id) {
        return service.approveApplication(id);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public LoanApplication reject(@PathVariable Long id) {
        return service.rejectApplication(id,"Application rejected by admin");
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public KYC verifyKYC(@RequestParam Long kycId, @RequestParam boolean isApproved) {
        if (isApproved) {
            return service.verifyKYC(kycId);
        } else {
            return service.rejectKYC(kycId, "KYC verification failed by admin");
        }
    }
}
