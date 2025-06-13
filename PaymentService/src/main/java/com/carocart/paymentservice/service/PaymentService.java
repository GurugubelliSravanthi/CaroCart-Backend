package com.carocart.paymentservice.service;

import com.carocart.paymentservice.dto.PaymentRequestDTO;
import com.carocart.paymentservice.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRequestDTO request);
}
