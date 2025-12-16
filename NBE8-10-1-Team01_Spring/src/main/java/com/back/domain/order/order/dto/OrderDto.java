package com.back.domain.order.order.dto;

import com.back.domain.order.order.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//order 자체에 대한 dto
public record OrderDto(
        Long orderId,
        Long userId,
        LocalDateTime createDate,
        List<OrderItemDto> items
) {
    public OrderDto(Orders order) {
        this(
                order.getId(),
                order.getUserId(),
                order.getCreateDate(),
                order.getOrderItems().stream()
                        .map(OrderItemDto::new)
                        .collect(Collectors.toList())
        );
    }
}


