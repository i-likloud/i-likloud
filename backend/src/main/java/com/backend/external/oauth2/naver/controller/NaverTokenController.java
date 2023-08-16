package com.backend.external.oauth2.naver.controller;

import com.backend.external.oauth2.naver.client.NaverTokenClient;
import com.backend.external.oauth2.naver.dto.NaverTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Tag(name = "Auth", description = "소셜로그인 관련 api")
@Controller
@RequiredArgsConstructor
public class NaverTokenController {

    private final NaverTokenClient naverTokenClient;

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    // 인가코드 콜백 결과
    @Operation(summary = "네이버 토큰", description = "네이버 로그인 토큰 메서드입니다.")
    @GetMapping("/oauth/naver/callback")
    public @ResponseBody String loginCallback(String code) {
        NaverTokenDto.Request naverTokenRequestDto = NaverTokenDto.Request.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(code)
                .state("navertest")
                .build();

        NaverTokenDto.Response naverToken = naverTokenClient.requestNaverToken(naverTokenRequestDto);

        return "nave token : " + naverToken;
    }
}
