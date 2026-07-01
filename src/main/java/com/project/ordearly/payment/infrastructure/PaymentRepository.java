package com.project.ordearly.payment.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ordearly.payment.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderId(Long orderId);
}