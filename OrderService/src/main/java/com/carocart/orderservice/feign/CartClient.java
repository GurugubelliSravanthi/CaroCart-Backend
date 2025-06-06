package com.carocart.orderservice.feign;

import com.carocart.orderservice.dto.CartItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "carocart-cart-service", url = "http://localhost:8083")
public interface CartClient {

    @GetMapping("/cart/get") // ✅ Matches your controller
    List<CartItemDTO> getUserCart(@RequestHeader("Authorization") String token);

    @DeleteMapping("/cart/clear")
    void clearCart(@RequestHeader("Authorization") String token);
}
