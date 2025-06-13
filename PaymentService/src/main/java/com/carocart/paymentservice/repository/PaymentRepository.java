package com.carocart.paymentservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.carocart.paymentservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
