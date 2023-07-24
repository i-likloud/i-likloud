package com.backend.oauth2.naver.service;

import com.backend.domain.member.constant.SocialType;
import com.backend.global.jwt.constant.GrantType;
import com.backend.oauth2.model.OauthAttributes;
import com.backend.oauth2.naver.client.NaverUserInfoClient;
import com.backend.oauth2.naver.dto.NaverUserInfoResponseDto;
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
public class NaverLoginApiServiceImpl implements SocialLoginApiService {

    private final NaverUserInfoClient naverUserInfoClient;

    // 네이버에서 받은 정보를 회원등록 폼에 맞춰서 반환
    @Override
    public OauthAttributes getUserInfo(String accessToken) {
        NaverUserInfoResponseDto naverUserInfoResponseDto = naverUserInfoClient.getNaverUserInfo(GrantType.BEARER.getType() + " " + accessToken);
        NaverUserInfoResponseDto.Response naverResponse = naverUserInfoResponseDto.getResponse();
        String email = naverResponse.getEmail();

        return OauthAttributes.builder()
                .email(!StringUtils.hasText(email) ? naverResponse.getEmail() : email)
                .nickname(naverResponse.getNickname())
                .socialType(SocialType.NAVER)
                .build();
    }
}
