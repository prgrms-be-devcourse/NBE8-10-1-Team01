package com.back.domain.customer.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "고객 로그인 응답")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerLoginResponse {

    @Schema(description = "고객 ID", example = "1")
    private Long customerId;

    @Schema(description = "응답 메시지", example = "로그인 완료되었습니다")
    private String message;

}
