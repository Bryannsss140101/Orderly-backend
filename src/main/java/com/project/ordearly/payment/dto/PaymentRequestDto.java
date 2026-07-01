package com.project.ordearly.payment.dto;

import java.math.BigDecimal;

import com.project.ordearly.payment.domain.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
public class PaymentRequestDto {
    @NotNull(message = "The payment method is mandatory")
    private PaymentMethod method;

    @NotNull(message = "The amount is mandatory")
    @DecimalMin(value = "0.1", message = "The amount must be greater than 0")
    private BigDecimal amount;
}