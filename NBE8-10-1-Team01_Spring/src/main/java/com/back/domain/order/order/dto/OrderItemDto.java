package com.back.domain.order.order.dto;

import com.back.domain.order.order.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public record OrderItemDto(
        Long productId,
        String productName,
        Integer count,
        Integer price,
        LocalDateTime createdAt
) {
    public OrderItemDto(OrderItem item) {
        this(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getCount(),
                item.getProduct().getPrice(),
                item.getOrders().getCreateDate()
        );
    }
}
