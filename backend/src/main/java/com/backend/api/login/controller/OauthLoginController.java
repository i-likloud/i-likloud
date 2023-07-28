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
    private final MemberService memberService;
    private final MemberInfoService memberInfoService;

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

    @Operation(summary = "은코인 1 상승", description = "게임에서 성공하면 은코인이 1 상승하는 메소드입니다.")
    @PostMapping("/silvercoinplus")
    public ResponseEntity<String> plusSilverCoin(@MemberInfo MemberInfoDto memberInfoDto){
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();
        memberInfoService.plusSilverCoin(memberId);
        return ResponseEntity.ok("은코인이 1 증가하였습니다.");
    }

    @Operation(summary = "은코인 5(임시) 삭제", description = "그림을 NFT화하면 은코인을 감소하는 메소드입니다.")
    @PostMapping("/silvercoinminus")
    public ResponseEntity<String> minusSilverCoin(@MemberInfo MemberInfoDto memberInfoDto) {
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();

        int currentSilverCoin = memberInfoService.getSilverCoin(memberId);
        int coinsToDeduct = 5;

        if (currentSilverCoin >= coinsToDeduct) {
            memberInfoService.minusSilverCoin(memberId, coinsToDeduct);
            return ResponseEntity.ok("은코인이 5 감소하였습니다");
        }
        else{
            return ResponseEntity.ok("은코인이 부족합니다");
        }

    }

}
