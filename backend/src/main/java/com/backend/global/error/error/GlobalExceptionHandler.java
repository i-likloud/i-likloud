package com.backend.global.error.error;

import com.backend.global.error.ErrorResponse;
import com.backend.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 실행 중 오류 발생
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<com.backend.global.error.ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException", e);
        com.backend.global.error.ErrorResponse errorResponse = com.backend.global.error.ErrorResponse.of(e.getErrorCode().getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    /**
     * 나머지 예외 발생
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<com.backend.global.error.ErrorResponse> handleException(Exception e) {
        log.error("Exception", e);
        com.backend.global.error.ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

    }
}
