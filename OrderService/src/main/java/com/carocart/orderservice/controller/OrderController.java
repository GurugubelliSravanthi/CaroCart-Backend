package com.carocart.orderservice.controller;

import com.carocart.orderservice.dto.OrderRequest;
import com.carocart.orderservice.dto.OrderResponse;
import com.carocart.orderservice.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestHeader("Authorization") String token,
                                                    @RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.placeOrder(token, orderRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@RequestHeader("Authorization") String token) {
        List<OrderResponse> orders = orderService.getOrdersForUser(token);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId,
                                                      @RequestHeader("Authorization") String token) {
        OrderResponse response = orderService.getOrderById(token, orderId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId,
                                              @RequestHeader("Authorization") String token) {
        orderService.cancelOrder(token, orderId);
        return ResponseEntity.ok("Order cancelled successfully.");
    }
    
 // --- ADMIN FUNCTIONALITY ---
    @GetMapping("/admin/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrdersForAdmin() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/admin/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderByIdForAdmin(@PathVariable Long orderId) {
        OrderResponse order = orderService.getOrderByIdForAdmin(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/admin/orders/{orderId}/cancel")
    public ResponseEntity<String> cancelOrderByAdmin(@PathVariable Long orderId) {
        orderService.cancelOrderByAdmin(orderId);
        return ResponseEntity.ok("Order cancelled by Admin successfully.");
    }
    
    @GetMapping("/debug/token")
    public ResponseEntity<String> debugToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok("Authorization header = " + authHeader);
    }
}
