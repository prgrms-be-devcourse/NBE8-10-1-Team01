package com.back.domain.order.order.dto;

import com.back.domain.order.order.entity.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 상품 정보")
public class OrderItemDto {

    @Schema(description = "상품 ID", example = "2")
    private Long productId;

    @Schema(description = "상품명", example = "수정")
    private String productName;

    @Schema(description = "수량", example = "2")
    private Integer count;

    @Schema(description = "상품 가격", example = "1000")
    private Integer price;

    public OrderItemDto() {
    }

    public OrderItemDto(OrderItem item) {
        this.productId = item.getProduct().getId();
        this.productName = item.getProduct().getName();
        this.count = item.getCount();
        this.price = item.getProduct().getPrice();
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getPrice() {
        return price;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
