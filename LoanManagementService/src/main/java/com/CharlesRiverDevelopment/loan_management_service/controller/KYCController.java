package com.CharlesRiverDevelopment.loan_management_service.controller;

import com.CharlesRiverDevelopment.loan_management_service.dto.KycRequestDTO;
import com.CharlesRiverDevelopment.loan_management_service.model.KYC;
import com.CharlesRiverDevelopment.loan_management_service.repository.KYCRepository;
import com.CharlesRiverDevelopment.loan_management_service.service.KYCService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
public class KYCController {

    private final KYCService kycService;
    private final KYCRepository kycRepository;
    @PostMapping("/create")
    public KYC createKYC(@RequestBody KycRequestDTO kyc) {
        return kycService.createKYC(kyc);
    }

    @GetMapping("/status")
    public String getKYCStatus(Long kycId) {
        KYC kyc = kycRepository.findById(kycId).orElseThrow(() -> new RuntimeException("KYC not found for id: " + kycId));
        // In a real application, you would check the KYC status from the database
        return "KYC status for user: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString() + " is: " + kyc.getStatus();
    }
}
