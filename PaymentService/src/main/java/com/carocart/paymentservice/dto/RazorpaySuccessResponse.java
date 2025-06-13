package com.carocart.paymentservice.dto;


public class RazorpaySuccessResponse {
    private String orderId;
    private String paymentId;
    private String signature;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

    // Getters and setters
    
}
