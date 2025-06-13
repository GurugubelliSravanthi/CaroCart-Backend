package com.carocart.orderservice.service;

import com.carocart.orderservice.dto.OrderRequest;
import com.carocart.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(String token, OrderRequest orderRequest);
    List<OrderResponse> getOrdersForUser(String token);
    OrderResponse getOrderById(String token, Long orderId);
    void cancelOrder(String token, Long orderId);
    
 // Admin-specific methods
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderByIdForAdmin(Long orderId);
    void cancelOrderByAdmin(Long orderId);
	void updatePaymentStatus(Long orderId, String paymentId, String status);

}
