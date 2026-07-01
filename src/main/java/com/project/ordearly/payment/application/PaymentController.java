package com.project.ordearly.payment.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ordearly.payment.domain.PaymentService;
import com.project.ordearly.payment.dto.PaymentRequestDto;
import com.project.ordearly.payment.dto.PaymentResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders/{orderId}/payments")
@PreAuthorize("isAuthenticated()")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> registerPayment(
            @PathVariable Long orderId,
            @Valid @RequestBody PaymentRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.registerPayment(orderId, requestDto));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> listPayments(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(paymentService.listPayments(orderId));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(
            @PathVariable Long orderId,
            @PathVariable Long paymentId) {

        paymentService.deletePayment(orderId, paymentId);

        return ResponseEntity.noContent().build();
    }
}