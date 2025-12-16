package com.back.domain.order.order.controller;

import com.back.domain.order.order.dto.OrderProductDto;
import com.back.domain.order.order.entity.OrderItem;
import com.back.domain.order.order.entity.Orders;
import com.back.domain.order.order.repository.OrderRepository;
import com.back.domain.order.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private Orders user1Order;
    private Orders user2Order;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();

        // user1: A 1개, B 2개
        user1Order = new Orders(1L);
        user1Order.getOrderItems().add(new OrderItem(user1Order, 1L, "A", 1, 1000));
        user1Order.getOrderItems().add(new OrderItem(user1Order, 2L, "B", 2, 2000));
        orderRepository.save(user1Order);

        // user2: A 3개, D 6개
        user2Order = new Orders(2L);
        user2Order.getOrderItems().add(new OrderItem(user2Order, 1L, "A", 3, 1000));
        user2Order.getOrderItems().add(new OrderItem(user2Order, 4L, "D", 6, 4000));
        orderRepository.save(user2Order);
    }

    @Test
    @DisplayName("모든 주문 조회")
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/users/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Orders> allOrders = orderService.findAll();
        assertThat(allOrders).hasSize(2);
    }


    @Transactional
    @Test
    @DisplayName("특정 사용자 주문 조회")
    void testGetUserOrders() throws Exception {
        mockMvc.perform(get("/api/users/1/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<OrderProductDto> userOrders = orderService.findByUserIdOrderByCreateDateDesc(1L)
                .stream()
                .flatMap(order -> order.getOrderItems().stream()
                        .map(OrderProductDto::new))
                .toList();

        assertThat(userOrders).hasSize(2);
        assertThat(userOrders.get(0).productName()).isEqualTo("A");
        assertThat(userOrders.get(1).productName()).isEqualTo("B");
    }
}