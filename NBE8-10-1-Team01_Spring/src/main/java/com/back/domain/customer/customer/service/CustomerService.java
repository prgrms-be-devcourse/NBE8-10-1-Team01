package com.back.domain.customer.customer.service;

import com.back.domain.customer.customer.dto.CustomerLoginRequest;
import com.back.domain.customer.customer.dto.CustomerLoginResponse;
import com.back.domain.customer.customer.entity.Customer;
import com.back.domain.customer.customer.repository.CustomerRepository;
import com.back.global.exception.customer.CustomerNotFoundException;
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
                    .customerId(null)
                    .message("관리자 로그인 완료되었습니다")
                    .build();
        }

        // 고객 이메일 체크 - DB에 존재하는지 확인
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("존재하지 않는 이메일입니다."));

        // 고객 리다이렉트
        return CustomerLoginResponse.builder()
                .customerId(customer.getId())
                .message("로그인 완료되었습니다")
                .build();
    }
}

