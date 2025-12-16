package com.back.domain.order.order.dto;

import com.back.domain.order.order.entity.Orders;

import java.time.LocalDateTime;

public record OrderDto(
        long orderId,
        long userId,
        LocalDateTime createDate
) {
    public OrderDto(Orders order) {
        this(
                order.getId(),
                order.getUserId(),
                order.getCreateDate()
        );
    }
}