package com.CharlesRiverDevelopment.loan_management_service.controller;

import com.CharlesRiverDevelopment.loan_management_service.model.LoanApplication;
import com.CharlesRiverDevelopment.loan_management_service.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
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
}
