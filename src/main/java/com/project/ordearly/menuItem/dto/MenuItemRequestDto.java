package com.project.ordearly.menuItem.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
public class MenuItemRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "The price is mandatory")
    @DecimalMin(value = "0.1", message = "The price must be greater than 0")
    private BigDecimal price;
}