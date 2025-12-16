package com.back.domain.product.product.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {
    private List<ProductDto> data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDto {
        private Long productId;
        private String name;
        private Integer price;
        private String image;
    }
}

