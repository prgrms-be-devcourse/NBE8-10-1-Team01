package com.back.domain.order.order.controller;

import com.back.domain.order.order.entity.Orders;
import com.back.domain.order.order.repository.OrderRepository;
import com.back.domain.order.order.service.OrderService;
import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.repository.ProductRepository;
import com.back.global.exception.order.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;


    private Orders testOrder;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();

        // Product 미리 생성
        Product product1 = Product.builder()
                .name("상품 A")
                .price(1000)
                .description("테스트 상품 A")
                .imagePath("/images/a.png")
                .build();

        Product product2 = Product.builder()
                .name("상품 B")
                .price(2000)
                .description("테스트 상품 B")
                .imagePath("/images/b.png")
                .build();

        productRepository.saveAll(List.of(product1, product2));

        // Orders 미리 생성
        testOrder = new Orders(1L); // userId = 1
        testOrder.addItem(product1, 3);
        testOrder.addItem(product2, 5);

        orderRepository.save(testOrder);
    }

    @Test
    @DisplayName("전체 주문 조회 성공")
    void getOrders_Success() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].orderId").value(testOrder.getId()));
    }

    @Test
    @DisplayName("특정 유저 주문 조회 성공")
    void getUserOrders_Success() throws Exception {
        mockMvc.perform(get("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(testOrder.getOrderItems().get(0).getProduct().getId()))
                .andExpect(jsonPath("$[0].productName").value("상품 A"))
                .andExpect(jsonPath("$[0].count").value(3))
                .andExpect(jsonPath("$[1].productName").value("상품 B"))
                .andExpect(jsonPath("$[1].count").value(5));
    }

    @Test
    @DisplayName("주문 삭제 성공")
    void deleteOrderTest() {
        // given: 삭제할 주문 미리 저장
        Orders savedOrder = orderRepository.save(new Orders(1L)); // userId=1인 주문 생성
        Long orderId = savedOrder.getId();

        // when: 서비스로 삭제
        orderService.deleteOrder(orderId);

        // then: 리포지토리에서 조회했을 때 없어야 함
        Optional<Orders> deletedOrder = orderRepository.findById(orderId);

        assertThat(deletedOrder).isEmpty();
    }

    @Test
    @DisplayName("주문 삭제 실패 - 존재하지 않는 ID")
    void deleteOrder_Fail() {
        // given
        Long nonExistentOrderId = 9999L;

        // when & then
        assertThatThrownBy(() -> orderService.deleteOrder(nonExistentOrderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("해당 주문이 없습니다. ID: " + nonExistentOrderId);
    }
}