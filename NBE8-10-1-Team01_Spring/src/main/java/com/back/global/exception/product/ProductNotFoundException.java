package com.back.global.exception.product;

/**
 * 상품을 찾을 수 없을 때 발생하는 예외
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * 에러 메시지를 포함하는 예외를 생성합니다.
     *
     * @param message 예외 상황을 설명하는 메시지
     */
    public ProductNotFoundException(String message) {
        super(message);
    }
}

