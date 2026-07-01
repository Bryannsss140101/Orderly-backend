package com.project.ordearly.payment.dto;

import java.math.BigDecimal;

import com.project.ordearly.payment.domain.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private Long id;
    private PaymentMethod method;
    private BigDecimal amount;
}