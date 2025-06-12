package com.carocart.address.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.carocart.address.DTO.UserResponseDTO;

@FeignClient(name = "carocart-authentication", url = "http://localhost:8081", contextId = "userClient")
public interface UserClient {

    @GetMapping("/users/me")
    UserResponseDTO getCurrentUser(@RequestHeader("Authorization") String token);
}
