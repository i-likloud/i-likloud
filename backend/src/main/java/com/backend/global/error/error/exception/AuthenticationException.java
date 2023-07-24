package com.backend.global.error.error.exception;


import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;

public class AuthenticationException extends BusinessException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

}
