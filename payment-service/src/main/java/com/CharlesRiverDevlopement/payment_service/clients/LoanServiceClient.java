package com.CharlesRiverDevlopement.payment_service.clients;

import com.CharlesRiverDevlopement.DTOs.EMIDetailsDTO;
import com.CharlesRiverDevlopement.payment_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "loan-management-service",
        url = "localhost:8081",  // Replace with the actual URL of the loan management service
        configuration = FeignConfig.class
)
public interface LoanServiceClient {

    @GetMapping("/api/loans/emi-schedule/{emiScheduleId}")
    EMIDetailsDTO getEMIDetails(
            @PathVariable Long emiScheduleId
    );
}