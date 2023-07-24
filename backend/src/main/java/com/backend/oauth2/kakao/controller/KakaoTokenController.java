package com.backend.oauth2.kakao.controller;

import com.backend.oauth2.kakao.client.KakaoTokenClient;
import com.backend.oauth2.kakao.dto.KakaoTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoTokenController {
    private final KakaoTokenClient kakaoTokenClient;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    // 로그인 폼
    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    // 인가코드 콜백 결과 ( 토큰값 있음)
    @GetMapping("/oauth/kakao/callback")
    public @ResponseBody String loginCallback(String code) {
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";
        KakaoTokenDto.Request kakaoTokenRequestDto = KakaoTokenDto.Request.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(code)
                .redirect_url("http://localhost:8080/oauth/kakao/callback")
                .build();

        KakaoTokenDto.Response kakaoToken = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto);

        return "kakao token : " + kakaoToken;
    }

}
