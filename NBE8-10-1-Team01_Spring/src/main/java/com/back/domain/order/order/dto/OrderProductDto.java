package com.back.domain.order.order.dto;

import com.back.domain.order.order.entity.OrderItem;
import java.time.LocalDateTime;

//주문 당시의 id나 이름 가격 등 snapshot 느낌의 dto
public record OrderProductDto(
        Long productId,
        String productName,
        Integer price,
        Integer count,
        Integer totalPrice,
        LocalDateTime createdAt
) {
    public OrderProductDto(OrderItem item) {
        this(
                item.getProductId(),
                item.getProductName(),
                item.getProductPrice(),
                item.getCount(),
                item.getProductPrice() * item.getCount(),
                item.getOrders().getCreateDate()
        );
    }
}