package com.back.domain.product.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private Long productId;
    private String name;
    private Integer price;
    private String description;
    private String image;
}

