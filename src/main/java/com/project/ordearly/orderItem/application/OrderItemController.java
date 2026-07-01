package com.project.ordearly.orderItem.application;

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

import com.project.ordearly.orderItem.domain.OrderItemService;
import com.project.ordearly.orderItem.dto.OrderItemRequestDto;
import com.project.ordearly.orderItem.dto.OrderItemResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders/{orderId}/items")
@PreAuthorize("isAuthenticated()")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @PostMapping
    public ResponseEntity<OrderItemResponseDto> addItem(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderItemService.addItem(orderId, requestDto));
    }

    @GetMapping
    public ResponseEntity<List<OrderItemResponseDto>> listItems(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(orderItemService.listItems(orderId));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {

        orderItemService.deleteItem(orderId, itemId);
        return ResponseEntity.noContent().build();
    }
}