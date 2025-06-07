package com.carocart.orderservice.entity;

//package: com.carocart.orderservice.entity

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private Long userId; // user placing the order

 private LocalDateTime orderDate;

 private String status; // e.g. PLACED, PROCESSING, SHIPPED, DELIVERED, CANCELLED

 private Double totalAmount;

 @Column(length = 1000)
 private String shippingAddress; // can store as JSON string or simple text

 private String paymentStatus; // PENDING, PAID, FAILED (optional)

 @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
 @JsonManagedReference

 private List<OrderItem> orderItems;

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
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

public String getPaymentStatus() {
	return paymentStatus;
}

public void setPaymentStatus(String paymentStatus) {
	this.paymentStatus = paymentStatus;
}

public List<OrderItem> getOrderItems() {
	return orderItems;
}

public void setOrderItems(List<OrderItem> orderItems) {
	this.orderItems = orderItems;
}

 // getters and setters
 
 
}
