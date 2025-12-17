package com.back.domain.order.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "리스트 응답 래퍼")
public class OrderListResponse<T> {

    @Schema(description = "응답 데이터")
    private final T data;

    public OrderListResponse(T data) {
        this.data = data;
    }

    public static <T> OrderListResponse<T> of(T data) {
        return new OrderListResponse<>(data);
    }

    public T getData() {
        return data;
    }
}