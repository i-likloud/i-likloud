package com.backend.global.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private final int httpStatusCode;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    public static ErrorResponse of(HttpStatus httpStatus, String errorCode, String message) {
        return ErrorResponse.builder()
                .httpStatusCode(httpStatus.value())
                .httpStatus(httpStatus)
                .errorCode(errorCode)
                .message(message)
                .build();
    }

    public static ErrorResponse of(String errorCode, BindingResult bindingResult) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(createErrorMessage(bindingResult))
                .build();
    }

    private static String createErrorMessage(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()){
            sb.append("[");
            sb.append(fieldError.getField());
            sb.append("](은)는 ");
            sb.append(fieldError.getDefaultMessage());
            sb.append(". ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


}
