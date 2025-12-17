package com.back.domain.order.order.service;

import com.back.domain.order.order.dto.OrderCreateRequest;
import com.back.domain.order.order.dto.OrderCreateResponse;
import com.back.domain.order.order.entity.Orders;
import com.back.domain.order.order.repository.OrderRepository;
import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Long productId;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setName("테스트상품");
        product.setPrice(1000);
        product.setDescription("설명");   // nullable이면 없어도 됨
        product.setImagePath("test.png"); // nullable이면 없어도 됨

        productId = productRepository.save(product).getId();
    }

    @Test
    @DisplayName("같은 고객이 연속으로 주문하면 같은 주문건으로 합쳐지고 아이템이 누적된다")
    void sameCustomer_twoCreateOrderCalls_shouldMergeIntoSingleOrder() {
        OrderCreateResponse r1 = orderService.createOrder(req("merge@test.com", productId, 1));
        OrderCreateResponse r2 = orderService.createOrder(req("merge@test.com", productId, 2));

        // ✅ 같은 구간이면 orderId가 같아야 함
        assertThat(r2.getOrderId()).isEqualTo(r1.getOrderId());

        // ✅ item 누적 확인 (addItem 호출 2번이면 2개)
        Orders saved = orderRepository.findById(r1.getOrderId()).orElseThrow();
        assertThat(saved.getOrderItems()).hasSize(2);
    }

    @Test
    @DisplayName("다른 고객이면 주문은 분리된다")
    void differentCustomer_shouldCreateDifferentOrder() {
        OrderCreateResponse r1 = orderService.createOrder(req("a@test.com", productId, 1));
        OrderCreateResponse r2 = orderService.createOrder(req("b@test.com", productId, 1));

        assertThat(r1.getOrderId()).isNotEqualTo(r2.getOrderId());
    }

    @Test
    @DisplayName("전날 14시~당일 14시 구간 계산: 13:59는 전날 14시, 14:00은 오늘 14시")
    void orderWindowStart_boundary_logicTest() throws Exception {
        Method m = OrderService.class.getDeclaredMethod("getOrderWindowStart", LocalDateTime.class);
        m.setAccessible(true);

        LocalDateTime t1 = LocalDateTime.of(2025, 12, 17, 13, 59, 59);
        LocalDateTime r1 = (LocalDateTime) m.invoke(orderService, t1);
        assertThat(r1).isEqualTo(LocalDateTime.of(2025, 12, 16, 14, 0, 0));

        LocalDateTime t2 = LocalDateTime.of(2025, 12, 17, 14, 0, 0);
        LocalDateTime r2 = (LocalDateTime) m.invoke(orderService, t2);
        assertThat(r2).isEqualTo(LocalDateTime.of(2025, 12, 17, 14, 0, 0));
    }

    private OrderCreateRequest req(String email, Long productId, int count) {
        OrderCreateRequest req = new OrderCreateRequest();

        OrderCreateRequest.CustomerDto c = new OrderCreateRequest.CustomerDto();
        c.setEmail(email);
        c.setAddress("서울시");
        c.setPostcode("12345");
        req.setCustomer(c);

        OrderCreateRequest.ProductDto p = new OrderCreateRequest.ProductDto();
        p.setProductId(productId);
        p.setCount(count);

        req.setProducts(List.of(p));
        return req;
    }
}