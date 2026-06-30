package com.project.ordearly.orderItem.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ordearly.orderItem.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}