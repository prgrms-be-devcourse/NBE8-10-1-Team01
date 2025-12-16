package com.back.domain.product.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateResponse {

    private Long productId;
    private String message;
}
