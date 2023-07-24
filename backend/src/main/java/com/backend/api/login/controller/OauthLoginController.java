package com.backend.api.login.controller;

import com.backend.api.login.dto.OauthLoginDto;
import com.backend.api.login.service.OauthLoginService;
import com.backend.api.login.validator.OauthValidator;
import com.backend.domain.member.constant.SocialType;
import com.backend.global.util.AuthorizationHeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OauthLoginController {

    private final OauthValidator oauthValidator;
    private final OauthLoginService oauthLoginService;

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
