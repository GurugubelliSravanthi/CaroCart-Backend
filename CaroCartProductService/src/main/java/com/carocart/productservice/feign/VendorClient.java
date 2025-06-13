package com.carocart.productservice.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.carocart.productservice.dto.VendorDTO;

@FeignClient(name = "carocart-authentication", url = "http://localhost:8081")
public interface VendorClient {
    
    @GetMapping("/vendors/me")
    VendorDTO getCurrentVendor(@RequestHeader("Authorization") String token);
}
