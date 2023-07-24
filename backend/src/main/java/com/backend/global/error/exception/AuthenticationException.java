package com.backend.global.error.exception;

import com.backend.global.error.ErrorCode;

public class AuthenticationException extends BusinessException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

}
