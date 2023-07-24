package com.backend.oauth2.naver.dto;

import lombok.Builder;
import lombok.Getter;

public class NaverTokenDto {

    // 토큰 요청 객체
    @Builder @Getter
    public static class Request {

        private String grant_type;
        private String client_id;
        private String client_secret;
        private String code;
        private String state;
    }

    // 응답 객체
    @Builder @Getter
    public static class Response {

        private String access_token;
        private String refresh_token;
        private String token_type;
        private Integer expires_in;
        private String error;
        private String error_description;
    }
}
