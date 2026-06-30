package com.project.ordearly.payment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ordearly.payment.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}