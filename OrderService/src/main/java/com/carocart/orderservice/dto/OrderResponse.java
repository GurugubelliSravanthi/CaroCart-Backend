// package: com.carocart.orderservice.dto

package com.carocart.orderservice.dto;

import com.carocart.orderservice.entity.Order;
import com.carocart.orderservice.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long orderId;
    private Long userId;
    private LocalDateTime orderDate;
    private String status;
    private Double totalAmount;
    private String shippingAddress;
    private List<OrderItemResponseDTO> orderItems;

    public OrderResponse() {}

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.userId = order.getUserId();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
        this.totalAmount = order.getTotalAmount();
        this.shippingAddress = order.getShippingAddress();
        this.orderItems = order.getOrderItems()
                .stream()
                .map(OrderItemResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Getters and setters

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItemResponseDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponseDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
