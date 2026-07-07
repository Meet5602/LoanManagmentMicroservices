package com.CharlesRiverDevelopment.loan_management_service.service;

import com.CharlesRiverDevelopment.loan_management_service.dto.KycRequestDTO;
import com.CharlesRiverDevelopment.loan_management_service.model.KYC;
import com.CharlesRiverDevelopment.loan_management_service.repository.KYCRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KYCService {

    private final KYCRepository kycRepository;
    public KYC createKYC(KycRequestDTO kyc) {

        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        //call the user-auth-service to validate the user exists and get user details
        // In a real application, you would save this to a database
        Optional<KYC> existingKyc =
                kycRepository.findByUserId(kyc.getUserId());
        if(existingKyc.isPresent()){
            throw new RuntimeException("KYC already exists for user: "+ kyc.getUserId() + " with email: " + email);
        }
        KYC newKyc = KYC.builder()
                .userId(kyc.getUserId())
                .fullName(kyc.getFullName())
                .address(kyc.getAddress())
                .phoneNumber(kyc.getPhoneNumber())
                .aadhaarNumber(kyc.getAadhaarNumber())
                .panNumber(kyc.getPanNumber())
                .address(kyc.getAddress())
                .submittedAt(java.time.LocalDateTime.now())
                .dateOfBirth(kyc.getDateOfBirth())
                .build();

        return kycRepository.save(newKyc);
    }

}
