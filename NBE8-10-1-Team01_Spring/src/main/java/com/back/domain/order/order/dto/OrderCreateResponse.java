package com.back.domain.order.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "주문 생성 응답")
public class OrderCreateResponse {

    @Schema(description = "주문 ID", example = "1")
    private Long orderId;

    @Schema(description = "고객 ID", example = "1")
    private Long customerId;

    @Schema(description = "주문 생성 일시", example = "2025-12-17T10:30:00")
    private LocalDateTime createDate;

    @Schema(description = "주문 상품 목록")
    private List<OrderItemDto> items;

    @Schema(description = "응답 메시지", example = "주문이 완료되었습니다")
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