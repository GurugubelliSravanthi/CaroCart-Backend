package com.carocart.paymentservice.controller;

import com.carocart.paymentservice.dto.RazorpaySuccessResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
public class PaymentController {
	 @Value("${razorpay.key_id}")
	    private String KEY_ID;

	    @Value("${razorpay.key_secret}")
	    private String KEY_SECRET;

    // ✅ Create Razorpay Order
    @PostMapping("/create-order")
    public ResponseEntity<String> createRazorpayOrder(@RequestParam double amount) {
        try {
            RazorpayClient client = new RazorpayClient(KEY_ID, KEY_SECRET);

            JSONObject options = new JSONObject();
            options.put("amount", (int)(amount * 100)); // Convert rupees to paise
            options.put("currency", "INR");
            options.put("receipt", "rcpt_" + System.currentTimeMillis());

            Order order = client.orders.create(options);

            return ResponseEntity.ok(order.toString()); // JSON string with orderId
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // ✅ Verify Payment and Notify OrderService
    @PutMapping("/verify-and-update")
    public ResponseEntity<String> verifyAndUpdatePayment(@RequestBody RazorpaySuccessResponse response) {
        try {
            // Assume signature verification is successful

            // Call OrderService to update status
            String orderServiceUrl = "http://OrderService/orders/" + response.getOrderId()
                + "/payment?paymentId=" + response.getPaymentId() + "&status=PAID";

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(orderServiceUrl, null);

            return ResponseEntity.ok("Order status updated to PAID");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update order: " + e.getMessage());
        }
    }

}
