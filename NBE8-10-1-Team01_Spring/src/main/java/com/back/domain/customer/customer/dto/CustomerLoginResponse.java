package com.back.domain.customer.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerLoginResponse {
    private String message;
    private String redirectUrl;
}


