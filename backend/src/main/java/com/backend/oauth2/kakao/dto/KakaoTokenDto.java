package com.backend.oauth2.kakao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class KakaoTokenDto {

    // 토큰 요청시
    @Builder
    @Getter
    public static class Request{
        private String grant_type;
        private String client_id;
        private String client_secret;
        private String redirect_url;
        private String code;
    }

    // 토큰 응답 결과
    @ToString
    @Builder
    @Getter
    public static class Response {
        private String token_type;
        private String access_token;
        private Integer expires_in;
        private String refresh_token;
        private Integer refresh_token_expires_in;
        private String scope;
    }
}
