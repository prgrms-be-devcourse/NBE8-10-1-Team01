package com.back.domain.order.order.service;

import com.back.domain.order.order.entity.Orders;
import com.back.domain.order.order.repository.OrderRepository;
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
}