package com.back.domain.customer.customer.controller;

import com.back.domain.customer.customer.dto.CustomerLoginRequest;
import com.back.domain.customer.customer.dto.CustomerLoginResponse;
import com.back.domain.customer.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerLoginResponse> login(@RequestBody CustomerLoginRequest request) {
        CustomerLoginResponse response = customerService.checkLogin(request);
        return ResponseEntity.ok(response);
    }
}

