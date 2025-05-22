package com.carocart.productservice.feign;

import com.carocart.productservice.dto.AdminResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "carocart-authentication", url = "http://localhost:8081", contextId = "adminClient")
public interface AdminClient {

    @GetMapping("/admins/me")
    AdminResponseDTO getCurrentAdmin(@RequestHeader("Authorization") String token);
}
