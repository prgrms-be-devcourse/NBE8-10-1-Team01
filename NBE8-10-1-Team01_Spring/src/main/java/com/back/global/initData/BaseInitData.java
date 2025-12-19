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
                    .email("DevCourseC1@gmail.com")
                    .address("서울시 강북구 삼양로 10-1")
                    .postcode("12345")
                    .build();

            Customer c2 = Customer.builder()
                    .email("DevCourseC2@gmail.com")
                    .address("부산시 해운대구 해변로 456")
                    .postcode("67890")
                    .build();

            customerRepository.saveAll(List.of(admin, c1, c2));

            // 2. 상품(Product) 3개 생성
            Product p1 = Product.builder()
                    .name("콜롬비아 원두")
                    .price(15000)
                    .description("라틴 아메리카 커피 원두")
                    .imagePath("bean1.png")
                    .build();

            Product p2 = Product.builder()
                    .name("아프리카 커피")
                    .price(18000)
                    .description("에티오피아 커피 원두")
                    .imagePath("bean2.webp")
                    .build();

            Product p3 = Product.builder()
                    .name("아시아 커피")
                    .price(12000)
                    .description("인도네시아 커피 원두")
                    .imagePath("bean3.jpg")
                    .build();

            productRepository.saveAll(List.of(p1, p2, p3));

            // 3. 주문(Orders) 생성 - 다양한 시간대로 설정

            // 주문 1: 고객1, 특정 시간 지정
            Orders order1 = new Orders();
            order1.assignCustomer(c1);
            order1.addItem(p1, 2);
            order1.addItem(p2, 1);
            order1.setCreateDateForTest(java.time.LocalDateTime.of(2025, 12, 22, 13, 30));

            // 주문 2: 고객2, 특정 시간 지정
            Orders order2 = new Orders();
            order2.assignCustomer(c1);
            order2.addItem(p3, 3);
            order2.setCreateDateForTest(java.time.LocalDateTime.of(2025, 12, 22, 13, 50));

            // 주문 3: 고객1, 특정 시간 지정
            Orders order3 = new Orders();
            order3.assignCustomer(c1);
            order3.addItem(p2, 2);
            order3.addItem(p3, 1);
            order3.setCreateDateForTest(java.time.LocalDateTime.of(2025, 12, 22, 14, 50));

            // 주문 4: 고객1, 특정 시간 지정
            Orders order4 = new Orders();
            order4.assignCustomer(c1);
            order4.addItem(p2, 2);
            order4.addItem(p3, 1);
            order4.setCreateDateForTest(java.time.LocalDateTime.of(2025, 12, 22, 16, 50));

            // 주문 5: 고객1, 특정 시간 지정
            Orders order5 = new Orders();
            order5.assignCustomer(c1);
            order5.addItem(p2, 2);
            order5.addItem(p3, 1);
            order5.setCreateDateForTest(java.time.LocalDateTime.of(2025, 12, 21, 13, 50));

            // 주문 6: 고객1, 특정 시간 지정
            Orders order6 = new Orders();
            order6.assignCustomer(c1);
            order6.addItem(p2, 2);
            order6.addItem(p3, 1);
            order6.setCreateDateForTest(java.time.LocalDateTime.of(2025, 12, 21, 16, 50));

            // 주문 7: 고객2, 현재 시간 기준 하루 전
            Orders order7 = new Orders();
            order7.assignCustomer(c2);
            order7.addItem(p1, 1);
            order7.addItem(p2, 1);
            order7.setCreateDateForTest(java.time.LocalDateTime.now().minusDays(1));

            // 주문 8: 고객2, 현재 시간 기준 5시간 전
            Orders order8 = new Orders();
            order8.assignCustomer(c2);
            order8.addItem(p3, 2);
            order8.setCreateDateForTest(java.time.LocalDateTime.now().minusHours(5));

            // 주문 9: 고객2, 현재 시간 기준 3시간 전
            Orders order9 = new Orders();
            order9.assignCustomer(c2);
            order9.addItem(p1, 1);
            order9.addItem(p3, 1);
            order9.setCreateDateForTest(java.time.LocalDateTime.now().minusHours(3));

            // 주문 10: 고객2, 현재 시간 기준 3시간 후
            Orders order10 = new Orders();
            order10.assignCustomer(c2);
            order10.addItem(p2, 3);
            order10.setCreateDateForTest(java.time.LocalDateTime.now().plusHours(3));

            // 주문 11: 고객2, 현재 시간 기준 5시간 후
            Orders order11 = new Orders();
            order11.assignCustomer(c2);
            order11.addItem(p1, 2);
            order11.addItem(p2, 1);
            order11.addItem(p3, 1);
            order11.setCreateDateForTest(java.time.LocalDateTime.now().plusHours(5));

            // 주문 12: 고객2, 현재 시간 기준 하루 후
            Orders order12 = new Orders();
            order12.assignCustomer(c2);
            order12.addItem(p3, 4);
            order12.setCreateDateForTest(java.time.LocalDateTime.now().plusDays(1));

            orderRepository.saveAll(List.of(order1, order2, order3, order4, order5, order6,
                                           order7, order8, order9, order10, order11, order12));
        };
    }
}
