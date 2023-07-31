package com.backend.api.login.dto;

import com.backend.global.jwt.dto.JwtDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

public class OauthLoginDto {

    @Getter
    @Setter
    public static class Request {
        private String email;
        private String socialType;
        private String firebaseToken;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String grantType;
        private String accessToken;
        private String refreshToken;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpirationPeriod;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date refreshTokenExpirationPeriod;

        public static Response of(JwtDto jwtDto) {
            return Response.builder()
                    .grantType(jwtDto.getGrantType())
                    .accessToken(jwtDto.getAccessToken())
                    .refreshToken(jwtDto.getRefreshToken())
                    .accessTokenExpirationPeriod(jwtDto.getAccessTokenExpirationPeriod())
                    .refreshTokenExpirationPeriod(jwtDto.getRefreshTokenExpirationPeriod())
                    .build();
        }

    }
}
