package com.backend.api.login.dto;

import com.backend.domain.member.constant.Role;
import com.backend.global.jwt.dto.JwtDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

public class OauthLoginDto {

    @Getter
    @Setter
    @Schema(description = "예시 DTO")
    public static class Request {
        @Schema(example = "testemail@test.com")
        private String email;

        @Schema(example = "KAKAO")
        private String socialType;

        @Schema(example = "")
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
        private Role role;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpirationPeriod;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date refreshTokenExpirationPeriod;

        public static Response of(JwtDto jwtDto, Role role) {
            return Response.builder()
                    .grantType(jwtDto.getGrantType())
                    .accessToken(jwtDto.getAccessToken())
                    .refreshToken(jwtDto.getRefreshToken())
                    .accessTokenExpirationPeriod(jwtDto.getAccessTokenExpirationPeriod())
                    .refreshTokenExpirationPeriod(jwtDto.getRefreshTokenExpirationPeriod())
                    .role(role)
                    .build();
        }

    }
}
