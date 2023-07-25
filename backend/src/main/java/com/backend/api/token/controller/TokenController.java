package com.backend.api.token.controller;

import com.backend.api.token.dto.AccessTokenResponseDto;
import com.backend.api.token.service.TokenService;
import com.backend.global.util.AuthorizationHeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "Token", description = "토큰 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class TokenController {

    private final TokenService tokenService;

    // refresh token 날아감
    @Operation(summary = "토큰 재발급", description = "access token 재발급 관련 메서드입니다.")
    @PostMapping("/access-token/re")
    public ResponseEntity<AccessTokenResponseDto> createAccessToken(HttpServletRequest httpServletRequest) {
        // 헤더 검증
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

        // refresh 추출 후 토큰 재발급으로 이동
        String refreshToken = authorizationHeader.split(" ")[1];
        AccessTokenResponseDto accessTokenResponseDto = tokenService.createAccessTokenByRefreshToken(refreshToken);
        return ResponseEntity.ok(accessTokenResponseDto);
    }
}
