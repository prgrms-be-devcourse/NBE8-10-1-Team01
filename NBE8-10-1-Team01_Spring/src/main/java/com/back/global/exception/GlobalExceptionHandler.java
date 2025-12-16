package com.back.global.exception;

import com.back.global.exception.customer.CustomerNotFoundException;
import com.back.global.exception.file.FileStorageException;
import com.back.global.exception.product.InvalidProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 핸들러
 * 애플리케이션에서 발생하는 모든 예외를 중앙에서 처리
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 상품 검증 실패 예외 처리
     * 상품 등록/수정 시 유효성 검증에 실패한 경우 처리
     *
     * @param ex 발생한 InvalidProductException
     * @return 400 Bad Request와 에러 메시지
     */
    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<Map<String, String>> handleInvalidProductException(InvalidProductException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 고객 미발견 예외 처리
     * 요청한 고객 정보를 찾을 수 없는 경우 처리
     *
     * @param ex 발생한 CustomerNotFoundException
     * @return 404 Not Found와 에러 메시지
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * 파일 저장 실패 예외 처리
     * 파일 업로드 및 저장 과정에서 오류가 발생한 경우 처리
     *
     * @param ex 발생한 FileStorageException
     * @return 500 Internal Server Error와 에러 메시지
     */
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<Map<String, String>> handleFileStorageException(FileStorageException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * 요청 데이터 검증 실패 예외 처리
     * {@code @Valid} 어노테이션을 통한 검증이 실패한 경우 처리
     *
     * @param ex 발생한 MethodArgumentNotValidException
     * @return 400 Bad Request와 필드별 에러 메시지 맵
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
