package com.backend.api.login.controller;

import com.backend.api.login.dto.OauthLoginDto;
import com.backend.api.login.service.OauthLoginService;
import com.backend.api.login.validator.OauthValidator;
import com.backend.api.member.service.MemberInfoService;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import com.backend.global.util.AuthorizationHeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "Auth", description = "소셜로그인 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OauthLoginController {

    private final OauthValidator oauthValidator;
    private final OauthLoginService oauthLoginService;

    @Operation(summary = "로그인", description = "로그인 및 회원가입 메서드입니다.")
    @PostMapping("/login")
    public ResponseEntity<OauthLoginDto.Response> oauthLogin(@RequestBody OauthLoginDto.Request oauthLoginRequestDto,
                                                             HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);
        oauthValidator.validateSocialType(oauthLoginRequestDto.getSocialType());

        String accessToken = authorizationHeader.split(" ")[1]; // 소셜에서 받은 accesstoken
        OauthLoginDto.Response jwtResponseDto = oauthLoginService.oauthLogin(accessToken,
                SocialType.from(oauthLoginRequestDto.getSocialType()));

        return ResponseEntity.ok(jwtResponseDto);
    }
}
