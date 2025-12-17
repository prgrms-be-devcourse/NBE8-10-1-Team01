package com.back.domain.order.order.controller;

import com.back.domain.customer.customer.entity.Customer;
import com.back.domain.order.order.dto.OrderCreateRequest;
import com.back.domain.order.order.dto.OrderCreateRequest.CustomerDto;
import com.back.domain.order.order.dto.OrderCreateRequest.ProductDto;
import com.back.domain.order.order.entity.Orders;
import com.back.domain.order.order.repository.OrderRepository;
import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
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
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private EntityManager em;

    private Customer testCustomer;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();

        // Customer
        testCustomer = Customer.builder()
                .email("test@example.com")
                .address("서울시 강남구")
                .postcode("12345")
                .build();
        em.persist(testCustomer);
        em.flush();

        // Products
        product1 = Product.builder()
                .name("상품 A")
                .price(1000)
                .description("테스트 상품 A")
                .imagePath("/images/a.png")
                .build();

        product2 = Product.builder()
                .name("상품 B")
                .price(2000)
                .description("테스트 상품 B")
                .imagePath("/images/b.png")
                .build();

        productRepository.saveAll(List.of(product1, product2));

        // 기존 주문 1건 (조회용)
        Orders order = new Orders();
        order.assignCustomer(testCustomer);
        order.addItem(product1, 3);
        order.addItem(product2, 5);
        orderRepository.save(order);

        em.flush();
        em.clear();
    }

    /* =====================================================
     * 헬퍼 메서드
     * ===================================================== */
    private OrderCreateRequest buildValidRequest(int count1, int count2) {
        CustomerDto customer = new CustomerDto();
        customer.setEmail("test@example.com");
        customer.setAddress("서울시 강남구");
        customer.setPostcode("12345");

        ProductDto p1 = new ProductDto();
        p1.setProductId(product1.getId());
        p1.setCount(count1);

        ProductDto p2 = new ProductDto();
        p2.setProductId(product2.getId());
        p2.setCount(count2);

        OrderCreateRequest request = new OrderCreateRequest();
        request.setCustomer(customer);
        request.setProducts(List.of(p1, p2));
        return request;
    }

    /* =====================================================
     * 조회 테스트
     * ===================================================== */

    @Test
    @DisplayName("전체 주문 조회 성공")
    void getOrders_Success() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("특정 고객 주문 조회 성공")
    void getUserOrders_Success() throws Exception {
        mockMvc.perform(get("/api/orders/" + testCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(product1.getId()))
                .andExpect(jsonPath("$[0].productName").value("상품 A"))
                .andExpect(jsonPath("$[0].count").value(3))
                .andExpect(jsonPath("$[1].productId").value(product2.getId()))
                .andExpect(jsonPath("$[1].productName").value("상품 B"))
                .andExpect(jsonPath("$[1].count").value(5));
    }

    /* =====================================================
     * 주문 생성 테스트
     * ===================================================== */

    @Test
    @DisplayName("주문 생성 성공 - 201 + orderId + items 검증")
    void createOrder_Success() throws Exception {
        OrderCreateRequest request = buildValidRequest(2, 3);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.orderId").isNumber())
                .andExpect(jsonPath("$.orderId").value(greaterThan(0)))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(2));
    }

    @Test
    @DisplayName("주문 생성 실패 - customer 누락 -> 400")
    void createOrder_Fail_WhenCustomerNull() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setCustomer(null);
        request.setProducts(List.of());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.customer").exists());
    }

    @Test
    @DisplayName("주문 생성 실패 - 이메일 공백 -> 400")
    void createOrder_Fail_WhenEmailBlank() throws Exception {
        OrderCreateRequest request = buildValidRequest(2, 3);
        request.getCustomer().setEmail(" ");

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.['customer.email']").exists());
    }

    @Test
    @DisplayName("주문 생성 실패 - products 비어있음 -> 400")
    void createOrder_Fail_WhenProductsEmpty() throws Exception {
        OrderCreateRequest request = buildValidRequest(2, 3);
        request.setProducts(List.of());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.products").exists());
    }

    @Test
    @DisplayName("주문 생성 실패 - count = 0 -> 400")
    void createOrder_Fail_WhenCountZero() throws Exception {
        OrderCreateRequest request = buildValidRequest(2, 3);
        request.getProducts().get(0).setCount(0);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문 생성 실패 - 존재하지 않는 상품 -> 404")
    void createOrder_Fail_WhenProductNotFound() throws Exception {
        OrderCreateRequest request = buildValidRequest(2, 3);
        request.getProducts().get(0).setProductId(999999L);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    /* =====================================================
     * 주문 삭제 테스트
     * ===================================================== */

    @Test
    @DisplayName("주문 삭제 성공 - 204")
    void deleteOrder_Success() throws Exception {
        Long orderId = orderRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/api/orders/" + orderId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("주문 삭제 실패 - 존재하지 않는 주문 -> 404")
    void deleteOrder_Fail_WhenNotFound() throws Exception {
        Long nonExistentId = 999999L;

        mockMvc.perform(delete("/api/orders/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }
}
