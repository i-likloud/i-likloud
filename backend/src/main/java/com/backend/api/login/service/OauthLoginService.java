package com.backend.api.login.service;

import com.backend.api.login.dto.OauthLoginDto;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.jwt.dto.JwtDto;
import com.backend.global.jwt.service.TokenManager;
import com.backend.oauth2.model.OauthAttributes;
import com.backend.oauth2.service.SocialLoginApiService;
import com.backend.oauth2.service.SocialLoginApiServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    public OauthLoginDto.Response oauthLogin(String accessToken, SocialType socialType) {
        SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(socialType);
        OauthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);
        log.info("userInfo : {}", userInfo);

        JwtDto jwtDto;
        Optional<Member> optionalMember = memberService.findByEmail(userInfo.getEmail());
        Member oauthMember;
        if (optionalMember.isEmpty()) { // 신규 회원가입
            oauthMember = userInfo.toMemberEntity(socialType);
            oauthMember = memberService.registerMember(oauthMember);
        } else { // 기존 회원
            oauthMember = optionalMember.get();
        }

        // 토큰 생성
        jwtDto = tokenManager.createJwtDto(oauthMember.getEmail(), oauthMember.getRole());
        oauthMember.updateRefreshToken(jwtDto);

        return OauthLoginDto.Response.of(jwtDto);
    }
}
