package com.project.ordearly.order.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ordearly.order.domain.Order;
import java.util.Optional;

import com.project.ordearly.order.domain.OrderType;
import com.project.ordearly.order.domain.OrderStatus;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByDailySummary_IdAndStatus(Long dailySummaryId, OrderStatus status);

    Optional<Order> findByTypeAndTableNumberAndStatus(
            OrderType type, Integer tableNumber, OrderStatus status);

    List<Order> findByStatus(OrderStatus status);
}