package com.project.ordearly.order.application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ordearly.order.domain.OrderService;
import com.project.ordearly.order.dto.OrderRequestDto;
import com.project.ordearly.order.dto.OrderResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@PreAuthorize("isAuthenticated()")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> openOrder(
            @Valid @RequestBody OrderRequestDto requestDto) {

        var order = orderService.openOrder(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id) {
        var order = orderService.getOrder(id);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/open")
    public ResponseEntity<List<OrderResponseDto>> getOpenTables() {
        var tables = orderService.getOpenTables();

        return ResponseEntity.ok(tables);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<OrderResponseDto> closeOrder(@PathVariable Long id) {
        var order = orderService.closeOrder(id);

        return ResponseEntity.ok(order);
    }
}