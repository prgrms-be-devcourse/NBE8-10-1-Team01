package com.back.global.exception.customer;

/**
 * 고객을 찾을 수 없을 때 발생하는 예외
 * 주어진 ID나 이메일로 고객 정보를 조회할 수 없을 때 사용
 */
public class CustomerNotFoundException extends RuntimeException {

    /**
     * 에러 메시지를 포함하는 예외를 생성
     *
     * @param message 예외 상황을 설명하는 메시지
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
