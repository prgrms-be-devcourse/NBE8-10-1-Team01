package com.back.domain.order.order.dto;

import com.back.domain.order.order.entity.Orders;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "주문 정보")
@Getter
@Setter
@NoArgsConstructor
public class OrderDto {

    @Schema(description = "주문 ID", example = "1")
    private long orderId;

    @Schema(description = "고객 ID", example = "1")
    private long customerId;

    @Schema(description = "고객 이메일", example = "test@example.com")
    private String customerEmail;

    @Schema(description = "고객 주소", example = "서울시 강남구")
    private String customerAddress;

    @Schema(description = "고객 우편번호", example = "12345")
    private String customerPostcode;

    @Schema(description = "주문 생성 일시", example = "2025-12-17T10:30:00")
    private LocalDateTime createDate;

    @Schema(description = "주문 상품 목록")
    private List<OrderItemDto> orderItems;

    public OrderDto(Orders order) {
        this.orderId = order.getId();
        this.customerId = order.getCustomer().getId();
        this.customerEmail = order.getCustomer().getEmail();
        this.customerAddress = order.getCustomer().getAddress();
        this.customerPostcode = order.getCustomer().getPostcode();
        this.createDate = order.getCreateDate();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());
    }
}