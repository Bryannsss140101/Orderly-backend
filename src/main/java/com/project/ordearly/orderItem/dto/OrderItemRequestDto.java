package com.project.ordearly.orderItem.dto;

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
public class OrderItemRequestDto {
    @NotNull(message = "The product is mandatory")
    private Long menuItemId;

    @NotNull(message = "The quantity is mandatory")
    @Min(value = 1, message = "The amount must be greater than 0")
    private Integer quantity;
}