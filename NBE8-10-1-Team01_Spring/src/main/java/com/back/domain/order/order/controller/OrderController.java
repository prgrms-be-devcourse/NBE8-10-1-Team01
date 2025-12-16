package com.back.domain.order.order.controller;

import com.back.domain.order.order.dto.OrderDto;
import com.back.domain.order.order.dto.OrderItemDto;
import com.back.domain.order.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Transactional(readOnly = true)
    public List<OrderDto> getOrders() {
        return orderService.findAll()
                .stream()
                .map(OrderDto::new)
                .toList();
    }

    @GetMapping("/{userId}")
    @Transactional(readOnly = true)
    public List<OrderItemDto> getUserOrders(@PathVariable Long userId) {
        return orderService.findByUserIdOrderByCreateDateDesc(userId)
                .stream()
                .flatMap(order ->
                        order.getOrderItems().stream()
                                .map(OrderItemDto::new)
                )
                .toList();
    }
}