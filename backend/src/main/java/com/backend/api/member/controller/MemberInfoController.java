package com.backend.api.member.controller;

import com.backend.api.member.dto.MemberDto;
import com.backend.api.member.dto.MemberInfoResponseDto;
import com.backend.api.member.dto.MemberSearchDto;
import com.backend.api.member.service.MemberInfoService;
import com.backend.api.token.dto.AccessTokenResponseDto;
import com.backend.api.token.service.TokenService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.member.service.MemberService;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

@Tag(name = "Member", description = "회원 관련 api")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberInfoController {

    private final TokenService tokenService;
    private final MemberInfoService memberInfoService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 회원 정보 조회
    @Operation(summary = "회원 정보", description = "회원가입후 회원 정보 조회 메서드입니다.")
    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponseDto> getMemberInfo(@MemberInfo MemberInfoDto memberInfoDto) {

        String email = memberInfoDto.getEmail();
        MemberInfoResponseDto memberInfoResponseDto = memberInfoService.getMemberInfo(email);

        return ResponseEntity.ok(memberInfoResponseDto);
    }

    // 추가정보 업데이트 메서드
    @Operation(summary = "추가 정보", description = "회원가입후 추가정보 관련 메서드입니다. \n\n " +
            "body 지우고 nickname, profileColor, profileFace, profileAccessory만 써서 요청해주세요")
    @PatchMapping("/additional")
    public ResponseEntity<AccessTokenResponseDto> updateAdditionalInfo(@RequestBody MemberDto.UpdateRequest request, @MemberInfo MemberInfoDto memberInfoDto) {
        // 닉네임 중복 검사
        Optional<Member> findByNickname = memberRepository.findByNickname(request.getNickname());
        if (findByNickname.isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED_NICKNAME);
        }
        MemberDto.Response response = memberInfoService.updateAdditionalInfo(request, memberInfoDto.getEmail());
        // 이메일로 멤버 찾아서 refresh token 가져오기
        String refreshToken = memberService.findMemberByEmail(memberInfoDto.getEmail()).getRefreshToken();
        // 엑세스 토큰 재발급
        AccessTokenResponseDto newToken = tokenService.createAccessTokenByRefreshToken(refreshToken);

        // 본문에는 response, 헤더에는 새 토큰을 추가하여 ResponseEntity 생성
        return ResponseEntity.ok()
                .header("newToken", newToken.getAccessToken())
                .body(newToken);
    }

    // 특정 멤버 조회
    @Operation(summary = "특정 멤버 정보", description = "멤버ID를 통해 관련 정보를 얻는 메소드입니다.")
    @GetMapping("/search/{memberId}")
    public ResponseEntity<MemberSearchDto> memberSearchInfo(@PathVariable Long memberId) {
        Member member = memberService.findMemberById(memberId);
        MemberSearchDto memberInfo = memberInfoService.getMember(member);
        return ResponseEntity.ok(memberInfo);
    }

}
