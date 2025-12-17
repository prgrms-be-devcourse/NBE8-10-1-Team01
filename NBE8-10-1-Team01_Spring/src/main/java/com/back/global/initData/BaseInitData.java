package com.back.global.initData;

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
    private final OrderRepository orderRepository;

    @Bean
    @Transactional
    public CommandLineRunner initData() {
        return args -> {

            if (productRepository.count() > 0) {
                return;
            }

            // 1. 상품(Product) 3개 생성
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
        };
    }
}
