package com.back.domain.order.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderCreateResponse {

    private Long orderId;
    private Long customerId;
    private LocalDateTime createDate;
    private List<OrderItemDto> items;
    private String message;

    public OrderCreateResponse() {
    }

    public Long getOrderId() { return orderId; }
    public Long getCustomerId() { return customerId; }
    public LocalDateTime getCreateDate() { return createDate; }
    public List<OrderItemDto> getItems() { return items; }
    public String getMessage() { return message; }

    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }
    public void setMessage(String message) { this.message = message; }
}