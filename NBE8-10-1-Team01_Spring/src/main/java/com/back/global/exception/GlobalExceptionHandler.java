package com.back.global.exception;

import com.back.global.exception.customer.CustomerNotFoundException;
import com.back.global.exception.file.FileStorageException;
import com.back.global.exception.order.OrderNotFoundException;
import com.back.global.exception.product.InvalidProductException;
import com.back.global.exception.product.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 핸들러
 * 애플리케이션에서 발생하는 모든 예외를 중앙에서 처리
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 상품 미발견 예외 처리
     * 요청한 상품 ID로 상품을 찾을 수 없는 경우 처리
     *
     * @param ex 발생한 ProductNotFoundException
     * @return 404 Not Found와 에러 메시지
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFoundException(ProductNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    /**
     * 주문 미발견 예외 처리
     * 요청한 주문 ID로 주문을 찾을 수 없는 경우 처리
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFoundException(OrderNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

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

    /**
     * 정적 리소스를 찾을 수 없을 때 예외 처리
     * Path Variable이 누락되거나 잘못된 URL 요청 시 처리
     *
     * @param ex 발생한 NoResourceFoundException
     * @return 404 Not Found와 에러 메시지
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFoundException(NoResourceFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        // /api/products/ 로 끝나는 경우 (productId 누락)
        if (ex.getResourcePath().contains("/api/products/")) {
            errorResponse.put("error", "상품 ID를 입력해주세요");
        } else {
            errorResponse.put("error", "요청한 리소스를 찾을 수 없습니다");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * 핸들러를 찾을 수 없을 때 예외 처리
     * 존재하지 않는 API 경로 요청 시 처리
     *
     * @param ex 발생한 NoHandlerFoundException
     * @return 404 Not Found와 에러 메시지
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "요청한 API를 찾을 수 없습니다");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * 필수 요청 파라미터 누락 예외 처리
     * @RequestParam으로 지정된 필수 파라미터가 없을 때 처리
     *
     * @param ex 발생한 MissingServletRequestParameterException
     * @return 400 Bad Request와 에러 메시지
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getParameterName() + "을(를) 입력해주세요");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 필수 파일 파트 누락 예외 처리
     * MultipartFile 업로드 시 파일이 없을 때 처리
     *
     * @param ex 발생한 MissingServletRequestPartException
     * @return 400 Bad Request와 에러 메시지
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestPartException(
            MissingServletRequestPartException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "파일을 업로드해주세요");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 메서드 인자 타입 불일치 예외 처리
     * Path Variable이나 RequestParam의 타입이 맞지 않을 때 처리
     *
     * @param ex 발생한 MethodArgumentTypeMismatchException
     * @return 400 Bad Request와 에러 메시지
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "잘못된 형식의 데이터입니다");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * HTTP 메시지 읽기 실패 예외 처리
     * JSON 파싱 실패 등의 경우 처리
     *
     * @param ex 발생한 HttpMessageNotReadableException
     * @return 400 Bad Request와 에러 메시지
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "요청 데이터 형식이 올바르지 않습니다");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 그 외 모든 예외 처리
     * 예상치 못한 서버 오류 처리
     *
     * @param ex 발생한 Exception
     * @return 500 Internal Server Error와 에러 메시지
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "서버 오류가 발생했습니다");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}