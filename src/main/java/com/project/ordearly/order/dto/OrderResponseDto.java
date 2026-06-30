package com.project.ordearly.order.dto;

import java.math.BigDecimal;
import java.util.List;

import com.project.ordearly.order.domain.OrderStatus;
import com.project.ordearly.order.domain.OrderType;
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
public class OrderResponseDto {
    private Long id;
    private OrderType type;
    private Integer tableNumber;
    private OrderStatus status;
    private BigDecimal total;
    private List<ItemDto> items;
    private List<PaymentDto> payments;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDto {
        private Long id;
        private String name;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDto {
        private Long id;
        private PaymentMethod method;
        private BigDecimal amount;
    }
}