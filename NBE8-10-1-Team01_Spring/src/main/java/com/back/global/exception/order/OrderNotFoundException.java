package com.back.global.exception.order;

/**
 * 주문을 찾을 수 없을 때 발생하는 예외
 * 주어진 주문 ID로 주문 정보를 조회할 수 없을 때 사용
 */
public class OrderNotFoundException extends RuntimeException {

    /**
     * 에러 메시지를 포함하는 예외를 생성합니다.
     *
     * @param message 예외 상황을 설명하는 메시지
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}