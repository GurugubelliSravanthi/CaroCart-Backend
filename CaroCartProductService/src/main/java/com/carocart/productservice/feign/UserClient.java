package com.carocart.productservice.feign;

import com.carocart.productservice.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "carocart-authentication", url = "http://localhost:8081", contextId = "userClient")
public interface UserClient {

    @GetMapping("/users/me")
    UserResponseDTO getCurrentUser(@RequestHeader("Authorization") String token);
}
