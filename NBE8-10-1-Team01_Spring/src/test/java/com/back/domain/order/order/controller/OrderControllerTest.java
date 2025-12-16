package com.back.domain.order.order.controller;

import com.back.domain.customer.customer.entity.Customer;
import com.back.domain.customer.customer.repository.CustomerRepository;
import com.back.domain.order.order.entity.Orders;
import com.back.domain.order.order.repository.OrderRepository;
import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;



import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Customer testCustomer;
    private Product product1;
    private Product product2;
    private Orders testOrder;

    @BeforeEach
    void setUp() {
        // 기존 데이터 삭제
        orderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();

        // 고객 생성
        testCustomer = Customer.builder()
                .email("test@example.com")
                .address("서울시 강남구")
                .postcode("12345")
                .build();
        testCustomer = customerRepository.save(testCustomer);

        // 상품 생성
        product1 = Product.builder()
                .name("상품1")
                .price(10000)
                .description("테스트 상품1")
                .imagePath("image1.jpg")
                .build();
        product2 = Product.builder()
                .name("상품2")
                .price(20000)
                .description("테스트 상품2")
                .imagePath("image2.jpg")
                .build();
        product1 = productRepository.save(product1);
        product2 = productRepository.save(product2);

        // 주문 생성
        testOrder = new Orders(testCustomer.getId());
        testOrder.addItem(product1, 2);
        testOrder.addItem(product2, 1);
        orderRepository.save(testOrder);
    }

    @Test
    @DisplayName("특정 유저 주문 조회 성공")
    void getUserOrders_Success() throws Exception {
        mockMvc.perform(get("/api/users/{userId}/orders", testCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3))) // 주문에 3개의 OrderItem
                .andExpect(jsonPath("$[0].productName", is("상품1")))
                .andExpect(jsonPath("$[0].count", is(2)))
                .andExpect(jsonPath("$[1].productName", is("상품2")))
                .andExpect(jsonPath("$[1].count", is(1)));
    }

    @Test
    @DisplayName("모든 주문 조회 성공")
    void getOrders_Success() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId", is(testCustomer.getId().intValue())))
                .andExpect(jsonPath("$[0].items", hasSize(2)));
    }
}