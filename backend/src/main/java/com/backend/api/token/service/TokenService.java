package com.backend.api.token.service;

import com.backend.api.token.dto.AccessTokenResponseDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.jwt.constant.GrantType;
import com.backend.global.jwt.service.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    // accessToken 재발급, refresh는 로그인때만 재발급
    public AccessTokenResponseDto createAccessTokenByRefreshToken(String refreshToken) {
        // refresh토큰으로 사용자 찾고
        Member member = memberService.findMemberByRefreshToken(refreshToken);

        // 만료기간, email, role 설정해서 access 토큰 재발급
        Date accessTokenExpirePeriod = tokenManager.createAccessTokenExpirationPeriod();
        String accessToken = tokenManager.createAccessToken(member.getEmail(), member.getRole(), accessTokenExpirePeriod);

        return AccessTokenResponseDto.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpirePeriod(accessTokenExpirePeriod)
                .build();
    }
}
