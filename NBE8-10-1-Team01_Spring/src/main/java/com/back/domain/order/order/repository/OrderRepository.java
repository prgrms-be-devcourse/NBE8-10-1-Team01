package com.back.domain.order.order.repository;

import com.back.domain.order.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserIdOrderByCreateDateDesc(Long userId);

}
