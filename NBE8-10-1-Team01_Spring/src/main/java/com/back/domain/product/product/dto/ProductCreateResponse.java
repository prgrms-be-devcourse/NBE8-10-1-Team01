package com.back.domain.product.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "상품 등록 응답")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateResponse {

    @Schema(description = "등록된 상품 ID", example = "3")
    private Long productId;

    @Schema(description = "응답 메시지", example = "상품 등록이 완료되었습니다")
    private String message;
}
