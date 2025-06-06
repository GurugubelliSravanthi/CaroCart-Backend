// package: com.carocart.orderservice.dto

package com.carocart.orderservice.dto;

import com.carocart.orderservice.entity.OrderItem;

public class OrderItemResponseDTO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double totalPrice;

    public OrderItemResponseDTO() {}

    public OrderItemResponseDTO(OrderItem item) {
        this.productId = item.getProductId();
        this.productName = item.getProductName();
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
        this.totalPrice = item.getTotalPrice();
    }

    // Getters and setters

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
