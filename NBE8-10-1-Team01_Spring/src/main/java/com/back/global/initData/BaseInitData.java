package com.back.global.initData;

import com.back.domain.customer.customer.entity.Customer;
import com.back.domain.customer.customer.repository.CustomerRepository;
import com.back.domain.order.order.entity.Orders;
import com.back.domain.order.order.repository.OrderRepository;
import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Bean
    @Transactional
    public CommandLineRunner initData() {
        return args -> {

            if (productRepository.count() > 0 || customerRepository.count() > 0) {
                return;
            }

            // 1. 고객 3명 생성 (관리자 + 일반고객 2명)
            Customer admin = Customer.builder()
                    .email("admin@DevCourse.com")   // 관리자 이메일 상수와 동일
                    .address("관리자 주소")
                    .postcode("00000")
                    .build();

            Customer c1 = Customer.builder()
                    .email("customer@test.com")
                    .address("서울시 강남구 테스트로 123")
                    .postcode("12345")
                    .build();

            Customer c2 = Customer.builder()
                    .email("customer2@test.com")
                    .address("부산시 해운대구 해변로 456")
                    .postcode("67890")
                    .build();

            customerRepository.saveAll(List.of(admin, c1, c2));

            // 2. 상품(Product) 3개 생성
            Product p1 = Product.builder()
                    .name("테스트 상품 1")
                    .price(15000)
                    .description("테스트 상품 설명 1")
                    .imagePath("coffee1.jpg")
                    .build();

            Product p2 = Product.builder()
                    .name("테스트 상품 2")
                    .price(18000)
                    .description("테스트 상품 설명 2")
                    .imagePath("coffee2.jpg")
                    .build();

            Product p3 = Product.builder()
                    .name("테스트 상품 3")
                    .price(12000)
                    .description("테스트 상품 설명 3")
                    .imagePath("coffee3.jpg")
                    .build();

            productRepository.saveAll(List.of(p1, p2, p3));

            // 고객1: 상품1 2개, 상품2 1개
            Orders order1 = new Orders();
            order1.assignCustomer(c1);
            order1.addItem(p1, 2);
            order1.addItem(p2, 1);

            // 고객2: 상품3 3개
            Orders order2 = new Orders();
            order2.assignCustomer(c2);
            order2.addItem(p3, 3);

            orderRepository.saveAll(List.of(order1, order2));
        };
    }
}
