package com.project.ordearly.summary.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.project.ordearly.order.dto.OrderResponseDto;

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
public class SummaryResponseDto {
    private Long id;
    private LocalDate date;
    private BigDecimal totalIncome;
    private BigDecimal totalYape;
    private BigDecimal totalCash;
    private List<OrderResponseDto> orders;
}