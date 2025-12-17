package com.back.domain.customer.customer.service;

import com.back.domain.customer.customer.dto.CustomerLoginRequest;
import com.back.domain.customer.customer.dto.CustomerLoginResponse;
import com.back.domain.customer.customer.entity.Customer;
import com.back.domain.customer.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    // 관리자 이메일 상수
    private static final String ADMIN_EMAIL = "admin@DevCourse.com";
    private static final String ADMIN_REDIRECT_URL = "/admin";
    private static final String CUSTOMER_REDIRECT_URL = "/orders";


    @Transactional
    public CustomerLoginResponse checkLogin(CustomerLoginRequest request) {
        String email = request.getEmail();

        // 관리자 이메일 체크
        if (ADMIN_EMAIL.equals(email)) {
            return CustomerLoginResponse.builder()
                    .message("로그인 완료되었습니다")
                    .redirectUrl(ADMIN_REDIRECT_URL)
                    .build();
        }

        // 고객 이메일 체크 - DB에 존재하는지 확인
        Customer customer = customerRepository.findByEmail(email)
                .orElseGet(() -> {
                    // 고객이 없으면 새로 생성 (간단한 회원가입)
                    Customer newCustomer = Customer.builder()
                            .email(email)
                            .address("")
                            .postcode("")
                            .build();
                    return customerRepository.save(newCustomer);
                });

        // 고객 리다이렉트
        return CustomerLoginResponse.builder()
                .message("로그인 완료되었습니다")
                .redirectUrl(CUSTOMER_REDIRECT_URL)
                .build();
    }
}

