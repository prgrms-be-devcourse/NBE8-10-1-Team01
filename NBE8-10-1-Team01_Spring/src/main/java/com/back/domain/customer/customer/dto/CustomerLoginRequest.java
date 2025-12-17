package com.back.domain.customer.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "고객 로그인 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLoginRequest {

    @Schema(description = "이메일", example = "customer@test.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}