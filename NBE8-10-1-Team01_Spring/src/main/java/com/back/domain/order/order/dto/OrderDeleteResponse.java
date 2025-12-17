package com.back.domain.order.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 삭제 응답 DTO
 */
@Schema(description = "주문 삭제 응답")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeleteResponse {

    @Schema(description = "응답 메시지", example = "주문 삭제가 완료되었습니다")
    private String message;
}


