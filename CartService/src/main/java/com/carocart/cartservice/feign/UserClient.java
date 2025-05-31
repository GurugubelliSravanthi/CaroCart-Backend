package com.carocart.cartservice.feign;

import com.carocart.cartservice.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "carocart-authentication", url = "http://localhost:8081", contextId = "userClient")
public interface UserClient {

    @GetMapping("/users/me")
    UserResponseDTO getCurrentUser(@RequestHeader("Authorization") String token);
    
    @GetMapping("/users/{userId}")
    UserResponseDTO getUserById(@PathVariable Long userId);

}
