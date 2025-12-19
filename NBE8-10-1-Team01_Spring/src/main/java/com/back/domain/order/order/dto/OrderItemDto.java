package com.back.domain.order.order.dto;

import com.back.domain.order.order.entity.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "주문 상품 정보")
@Getter
@Setter
@NoArgsConstructor
public class OrderItemDto {

    @Schema(description = "상품 ID", example = "2")
    private Long productId;

    @Schema(description = "상품명", example = "수정")
    private String productName;

    @Schema(description = "수량", example = "2")
    private Integer count;

    @Schema(description = "상품 가격", example = "1000")
    private Integer price;

    // 기존 생성자 유지 (다른 코드 영향 없음)
    public OrderItemDto(OrderItem item) {
        this.productId = item.getProduct().getId();
        this.productName = item.getProduct().getName();
        this.count = item.getCount();
        this.price = item.getProduct().getPrice();
    }
}
