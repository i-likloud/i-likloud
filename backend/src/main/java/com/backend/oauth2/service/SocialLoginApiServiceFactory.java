package com.backend.oauth2.service;

import com.backend.domain.member.constant.SocialType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SocialLoginApiServiceFactory {

    private static Map<String, SocialLoginApiService> socialLoginApiServices;

    public SocialLoginApiServiceFactory(Map<String, SocialLoginApiService> socialLoginApiServices) {
        this.socialLoginApiServices = socialLoginApiServices;
    }

    // 소셜 타입에 맞춰서 저장
    public static SocialLoginApiService getSocialLoginApiService(SocialType socialType) {
        String socialLoginApiServiceBeanName = "";

        if (SocialType.KAKAO.equals(socialType)) {
            socialLoginApiServiceBeanName = "kakaoLoginApiServiceImpl";
        } else if (SocialType.NAVER.equals(socialType)) {
            socialLoginApiServiceBeanName = "naverLoginApiServiceImpl";
        }

        return socialLoginApiServices.get(socialLoginApiServiceBeanName);
    }
}
