package com.back.domain.order.order.controller;

import com.back.domain.order.order.dto.OrderDto;
import com.back.domain.order.order.dto.OrderProductDto;

import com.back.domain.order.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //모든 주문 조회
    @GetMapping("/api/users/orders")
    @Transactional
    public List<OrderDto> getOrders() {
        return orderService.findAll()
                .stream()
                .map(order -> new OrderDto(
                        order.getId(),
                        order.getUserId(),
                        order.getCreateDate()
                ))
                .toList();
    }

    //특정 유저 조회
    @GetMapping("/api/users/{userId}/orders")
    @Transactional
    public List<OrderProductDto> getUserOrders(@PathVariable Long userId) {
        return orderService.findByUserIdOrderByCreateDateDesc(userId)
                .stream()
                .flatMap(order ->
                        order.getOrderItems().stream()
                                .map(OrderProductDto::new)
                )
                .toList();
    }
}