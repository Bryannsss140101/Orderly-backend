package com.project.ordearly.order.domain;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.ordearly.exceptions.BadRequestException;
import com.project.ordearly.exceptions.ResourceNotFoundException;
import com.project.ordearly.order.dto.OrderRequestDto;
import com.project.ordearly.order.dto.OrderResponseDto;
import com.project.ordearly.order.infrastructure.OrderRepository;
import com.project.ordearly.summary.domain.Summary;
import com.project.ordearly.summary.infrastructure.SummaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final SummaryRepository summaryRepository;

    public OrderResponseDto openOrder(OrderRequestDto requestDto) {
        if (requestDto.getType() == OrderType.TABLE && requestDto.getTableNumber() == null)
            throw new BadRequestException("You must indicate the table number");

        if (requestDto.getType() == OrderType.TABLE) {
            orderRepository.findByTypeAndTableNumberAndStatus(
                    OrderType.TABLE,
                    requestDto.getTableNumber(),
                    OrderStatus.OPEN)
                    .ifPresent(o -> {
                        throw new BadRequestException(
                                "The table" + requestDto.getTableNumber() + " is already open");
                    });
        }

        var summary = summaryRepository.findByDate(LocalDate.now())
                .orElseGet(() -> summaryRepository.save(Summary.builder()
                        .date(LocalDate.now())
                        .build()));

        var order = Order.builder()
                .dailySummary(summary)
                .type(requestDto.getType())
                .tableNumber(requestDto.getTableNumber())
                .build();

        return modelMapper.map(orderRepository.save(order), OrderResponseDto.class);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order not found"));

        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOpenTables() {
        return orderRepository.findByStatus(OrderStatus.OPEN)
                .stream()
                .map(o -> modelMapper.map(o, OrderResponseDto.class))
                .toList();
    }

    public OrderResponseDto closeOrder(Long id) {
        var orden = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        orden.setStatus(OrderStatus.PAID);
        return modelMapper.map(orderRepository.save(orden), OrderResponseDto.class);
    }
}