package com.backend.oauth2.kakao.service;

import com.backend.domain.member.constant.SocialType;
import com.backend.global.jwt.constant.GrantType;
import com.backend.oauth2.kakao.client.KakaoUserInfoClient;
import com.backend.oauth2.kakao.dto.KakaoUserInfoResponseDto;
import com.backend.oauth2.model.OauthAttributes;
import com.backend.oauth2.service.SocialLoginApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoLoginApiServiceImpl implements SocialLoginApiService {

    private final KakaoUserInfoClient kakaoUserInfoClient;
    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf8";

    @Override
    public OauthAttributes getUserInfo(String accessToken) { // kakao에서 받은 accessToken
        KakaoUserInfoResponseDto kakaoUserInfoResponseDto = kakaoUserInfoClient.getKakaoUserInfo(CONTENT_TYPE,
                GrantType.BEARER.getType() + " " + accessToken);
        KakaoUserInfoResponseDto.KakaoAccount kakaoAccount = kakaoUserInfoResponseDto.getKakaoAccount();
        String email = kakaoAccount.getEmail();

        // 받은 정보로 회원 등록 폼에 맞춰 반환
        return OauthAttributes.builder()
                .email(!StringUtils.hasText(email) ? kakaoUserInfoResponseDto.getId() : email) // 카카오는 이메일 받아오기 안되면 일단 ID를 이메일에 저장
                .nickname(kakaoAccount.getProfile().getNickname())
                .socialType(SocialType.KAKAO)
                .build();

    }
}
