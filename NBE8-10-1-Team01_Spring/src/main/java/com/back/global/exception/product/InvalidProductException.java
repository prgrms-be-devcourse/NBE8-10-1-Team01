package com.back.global.exception.product;

/**
 * 상품 데이터 검증 실패 시 발생하는 예외
 * 상품명, 가격 등의 필수 필드가 누락되거나 유효하지 않을 때 사용
 */
public class InvalidProductException extends RuntimeException {

    /**
     * 에러 메시지를 포함하는 예외를 생성
     *
     * @param message 검증 실패 원인을 설명하는 메시지
     */
    public InvalidProductException(String message) {
        super(message);
    }
}

