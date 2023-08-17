package com.backend.external.oauth2.naver.client;

import com.backend.external.oauth2.naver.dto.NaverUserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://openapi.naver.com", name = "naverUserInfoClient")
public interface NaverUserInfoClient {

    // 회원정보 요청
    @GetMapping(value = "/v1/nid/me", consumes = "application/json")
    NaverUserInfoResponseDto getNaverUserInfo(@RequestHeader("Authorization") String accessToken);
}
