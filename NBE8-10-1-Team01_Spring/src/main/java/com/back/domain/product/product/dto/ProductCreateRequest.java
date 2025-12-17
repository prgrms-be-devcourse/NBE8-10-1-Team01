package com.back.domain.product.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Schema(description = "상품 등록 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateRequest {

    @Schema(description = "상품명", example = "선풍기", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "상품명은 필수입니다.")
    private String name;

    @Schema(description = "상품 가격", example = "50000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 0보다 커야 합니다.")
    private Integer price;

    @Schema(description = "상품 설명", example = "시원한 선풍기입니다")
    private String description;
}
