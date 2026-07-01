package com.project.ordearly.payment.domain;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.ordearly.exceptions.BadRequestException;
import com.project.ordearly.exceptions.ResourceNotFoundException;
import com.project.ordearly.order.domain.OrderStatus;
import com.project.ordearly.order.infrastructure.OrderRepository;
import com.project.ordearly.payment.dto.PaymentRequestDto;
import com.project.ordearly.payment.dto.PaymentResponseDto;
import com.project.ordearly.payment.infrastructure.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
    private final ModelMapper modelMapper;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentResponseDto registerPayment(
            Long orderId, PaymentRequestDto requestDto) {

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.OPEN)
            throw new BadRequestException("A payment cannot be recorded on a closed order");

        var payment = Payment.builder()
                .order(order)
                .method(requestDto.getMethod())
                .amount(requestDto.getAmount())
                .build();

        return modelMapper.map(paymentRepository.save(payment), PaymentResponseDto.class);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDto> listPayments(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(p -> modelMapper.map(p, PaymentResponseDto.class))
                .toList();
    }

    public void deletePayment(Long orderId, Long paymentId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.OPEN)
            throw new BadRequestException("A payment cannot be recorded on a closed order");

        var payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        paymentRepository.delete(payment);
    }
}