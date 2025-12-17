package com.back.domain.product.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "상품 수정 요청")
@Getter
@Setter
public class ProductUpdateRequest {

    @Schema(description = "상품명", example = "수정된 상품명")
    private String name;

    @Schema(description = "상품 가격", example = "1000")
    private Integer price;

    @Schema(description = "상품 설명", example = "수정된 상품 설명입니다")
    private String description;
}
