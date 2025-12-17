package com.back.domain.product.product.dto;

import lombok.*;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 목록 응답")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {

    @Schema(
            description = "상품 목록",
            // 배열 요소 타입 힌트 (중첩 클래스라 implementation까진 보통 컨트롤러에서 잡아줌)
            implementation = ProductListResponse.ProductDto.class
    )
    private List<ProductDto> data;

    @Schema(description = "상품 요약 정보")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDto {

        @Schema(description = "상품 ID", example = "2")
        private Long productId;

        @Schema(description = "상품명", example = "수정")
        private String name;

        @Schema(description = "상품 가격", example = "1000")
        private Integer price;

        @Schema(description = "상품 이미지 URL",
                example = "/api/products/images/27f59056-b059-408e-a9ff-54078d07e15e.png")
        private String image;
    }
}


