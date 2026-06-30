package com.project.ordearly.order.dto;

import com.project.ordearly.order.domain.OrderType;

import jakarta.validation.constraints.Min;
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
public class OrderRequestDto {
    @NotNull(message = "The order type is mandatory")
    private OrderType type;

    @Min(value = 1, message = "The table number must be greater than 0")
    private Integer tableNumber;
}