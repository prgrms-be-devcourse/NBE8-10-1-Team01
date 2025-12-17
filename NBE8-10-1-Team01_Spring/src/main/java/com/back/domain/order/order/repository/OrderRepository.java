package com.back.domain.order.order.repository;

import com.back.domain.order.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    // Orders.customer.id 를 의미 (중첩 프로퍼티)
    List<Orders> findByCustomer_IdOrderByCreateDateDesc(Long customerId);
}