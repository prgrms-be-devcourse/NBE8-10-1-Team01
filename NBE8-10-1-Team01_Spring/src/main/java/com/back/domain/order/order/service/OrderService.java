package com.back.domain.order.order.service;

import com.back.domain.order.order.entity.Orders;
import com.back.domain.order.order.repository.OrderRepository;
import com.back.global.exception.customer.CustomerNotFoundException;
import com.back.global.exception.order.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Orders> findAll() {
        return orderRepository.findAll();
    }

    public List<Orders> findByUserIdOrderByCreateDateDesc(Long userId) {
        return orderRepository.findByUserIdOrderByCreateDateDesc(userId);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("해당 주문이 없습니다. ID: " + orderId));
        orderRepository.delete(order);
        }
    }