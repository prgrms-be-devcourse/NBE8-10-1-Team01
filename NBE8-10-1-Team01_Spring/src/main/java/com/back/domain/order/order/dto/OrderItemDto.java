package com.back.domain.order.order.dto;

import com.back.domain.order.order.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderItemDto {

    private Long productId;
    private String productName;
    private Integer count;
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
