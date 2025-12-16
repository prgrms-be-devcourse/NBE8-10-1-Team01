package com.back.domain.order.order.dto;

import com.back.domain.order.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

// 주문 당시의 상품 정보 snapshot
public record OrderItemDto(
        Long orderItemId,
        Long productId,
        String productName,
        Integer productPrice,
        String productImagePath,
        Integer count,
        LocalDateTime createdAt
) {
    public OrderItemDto(OrderItem item) {
        this(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getProduct().getImagePath(),
                item.getCount(),
                item.getOrders().getCreateDate()
        );
    }
}