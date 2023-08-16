package com.backend.external.oauth2.naver.client;

import com.backend.external.oauth2.naver.dto.NaverTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "https://nid.naver.com", name = "naverTokenClient")
public interface NaverTokenClient {

    // 로그인 토큰 요청
    @PostMapping(value = "/oauth2.0/token", consumes = "application/json")
    NaverTokenDto.Response requestNaverToken(@SpringQueryMap NaverTokenDto.Request request);
}
