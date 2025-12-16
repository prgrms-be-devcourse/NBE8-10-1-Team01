package com.back.global.exception.file;

/**
 * 파일 저장 중 발생하는 예외
 * 파일 업로드, 저장, 디렉토리 생성 등의 작업이 실패했을 때 사용
 */
public class FileStorageException extends RuntimeException {

    /**
     * 에러 메시지를 포함하는 예외를 생성
     *
     * @param message 예외 상황을 설명하는 메시지
     */
    public FileStorageException(String message) {
        super(message);
    }

    /**
     * 에러 메시지와 원인 예외를 포함하는 예외를 생성
     *
     * @param message 예외 상황을 설명하는 메시지
     * @param cause 이 예외의 원인이 되는 예외
     */
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
