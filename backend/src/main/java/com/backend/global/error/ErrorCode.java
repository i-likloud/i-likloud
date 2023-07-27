package com.backend.global.error;

import org.springframework.http.HttpStatus;


public enum ErrorCode {

    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "001", "business exception test"),

    // 인증 && 인가
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Auth-001", "토큰이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Auth-002", "해당 refresh token은 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Auth-003", "해당 refresh token이 존재하지 않습니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "Auth-004", "해당 토큰은 ACCESS TOKEN이 아닙니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Auth-005", "해당 토큰은 유효한 토큰이 아닙니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "Auth-006", "Authorization Header가 없습니다."),
    NOT_MATCH_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "Auth-007", "인증 타입이 Bearer 타입이 아닙니다."),
    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN, "Auth-008", "관리자가 아닙니다."),
    FORBIDDEN_MEMBER(HttpStatus.FORBIDDEN, "Auth-009", "회원이 아닙니다. 추가정보를 입력해 주세요."),

    // 회원
    INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, "User-001", "잘못된 회원 타입 입니다."),
    ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, "User-002", "이미 가입된 회원 입니다."),
    ALREADY_REGISTERED_NICKNAME(HttpStatus.BAD_REQUEST, "User-003", "이미 등록된 닉네임입니다."),
    MEMBER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "User-004", "해당 회원은 존재하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "User-005", "인증에 실패하였습니다."),
    ROLE_NOT_EXISTS(HttpStatus.FORBIDDEN, "User-006", "해당 권한이 없습니다."),

    // 그림

    // 사진


    // 파일
    FILE_UPLOAD_CONFLICT(HttpStatus.CONFLICT, "File-001", "파일 업로드에 실패하였습니다."),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "File-002", " 파일 이름이 올바르지 않습니다."),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "File-003", "파일을 찾을 수 없습니다."),

    ;

    private HttpStatus httpStatus;
    private String errorCode;
    private String description;

    ErrorCode(HttpStatus httpStatus, String errorCode, String description) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.description = description;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }
}
