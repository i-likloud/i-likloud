package com.backend.oauth2.kakao.controller;

import com.backend.oauth2.kakao.client.KakaoTokenClient;
import com.backend.oauth2.kakao.dto.KakaoTokenDto;
import com.backend.oauth2.nft.client.WalletClient;
import com.backend.oauth2.nft.dto.WalletDto;
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
public class KakaoTokenController {
    private final KakaoTokenClient kakaoTokenClient;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    // 지갑 발금
    private final WalletClient walletClient;


    @Value("${KAS.client.accessKeyId}")
    private String accessKeyId;

    @Value("${KAS.client.secretAccessKey}")
    private String secretAccessKey;

    @Value("${KAS.client.authorization}")
    private String authorization;

    // 로그인 폼
    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    // 인가코드 콜백 결과 ( 토큰값 있음)
    @Operation(summary = "카카오 토큰", description = "카카오 로그인 토큰 메서드입니다.")
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


        WalletDto.Request walletRequestDto = WalletDto.Request.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeyId)
                .authorizationHeader(authorization)
                .xChainId("1001")
                .build();

        String walletResponse = walletClient.createWallet(walletRequestDto.getXChainId(), walletRequestDto.getAuthorizationHeader());


        return "kakao token : " + kakaoToken;
    }

}
