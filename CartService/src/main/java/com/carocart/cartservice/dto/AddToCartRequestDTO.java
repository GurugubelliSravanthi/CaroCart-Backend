package com.carocart.cartservice.dto;

public class AddToCartRequestDTO {

    private Long userId;      // Add this field
    private Long productId;
    private int quantity;

    public AddToCartRequestDTO() {
    }

    public AddToCartRequestDTO(Long userId, Long productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
