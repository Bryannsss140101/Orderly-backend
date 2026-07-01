package com.project.ordearly.orderItem.domain;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.ordearly.exceptions.BadRequestException;
import com.project.ordearly.exceptions.ResourceNotFoundException;
import com.project.ordearly.menuItem.infrastructure.MenuItemRepository;
import com.project.ordearly.order.domain.Order;
import com.project.ordearly.order.domain.OrderStatus;
import com.project.ordearly.order.infrastructure.OrderRepository;
import com.project.ordearly.orderItem.dto.OrderItemRequestDto;
import com.project.ordearly.orderItem.dto.OrderItemResponseDto;
import com.project.ordearly.orderItem.infrastructure.OrderItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final ModelMapper modelMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderItemResponseDto addItem(
            Long orderId, OrderItemRequestDto requestDto) {

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.OPEN)
            throw new BadRequestException("Items cannot be added to a closed order");

        var product = menuItemRepository.findById(requestDto.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        var items = orderItemRepository.findByOrderId(orderId);

        var itemExist = items.stream()
                .filter(i -> i.getMenuItem() != null &&
                        i.getMenuItem().getId().equals(requestDto.getMenuItemId()))
                .findFirst();

        OrderItem item;

        if (itemExist.isPresent()) {
            item = itemExist.get();

            var amount = item.getQuantity() + requestDto.getQuantity();

            item.setQuantity(amount);
            item.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(amount)));
        } else {
            item = OrderItem.builder()
                    .order(order)
                    .menuItem(product)
                    .name(product.getName())
                    .quantity(requestDto.getQuantity())
                    .unitPrice(product.getPrice())
                    .subtotal(product.getPrice().multiply(BigDecimal.valueOf(requestDto.getQuantity())))
                    .build();
        }

        var save = orderItemRepository.save(item);
        updateTotal(order);

        return modelMapper.map(save, OrderItemResponseDto.class);
    }

    public void deleteItem(Long orderId, Long itemId) {

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.OPEN)
            throw new BadRequestException("Items cannot be removed from a closed order");

        var item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        orderItemRepository.delete(item);
        updateTotal(order);
    }

    @Transactional(readOnly = true)
    public List<OrderItemResponseDto> listItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(i -> modelMapper.map(i, OrderItemResponseDto.class))
                .toList();
    }

    private void updateTotal(Order order) {
        var total = orderItemRepository.findByOrderId(order.getId())
                .stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
        orderRepository.save(order);
    }
}