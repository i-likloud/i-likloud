package com.backend.domain.member.controller;

import com.backend.domain.member.dto.MypageInfoDto;
import com.backend.domain.member.dto.ProfileDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.member.service.MypageService;
import com.backend.domain.member.service.ProfileService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Tag(name = "Mypage", description = "마이페이지 관련 api")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;
    private final ProfileService profileService;
    private final MemberService memberService;


    @Operation(summary = "마이페이지 홈 조회", description = "마이페이지 홈의 조회 메서드입니다.")
    @GetMapping("/home")
    public ResponseEntity<MypageInfoDto> getMyInfo(@MemberInfo MemberInfoDto memberInfoDto){
        try {
//            String userEmail = request.getRemoteUser();
            String email = memberInfoDto.getEmail();
            log.info(email);
            MypageInfoDto myPageInfoDto = mypageService.getMyInfo(email);
            return ResponseEntity.ok(myPageInfoDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "닉네임 수정", description = "프로필의 닉네임 수정 메서드입니다.")
    @PutMapping("/nickname")
    public ResponseEntity<Member> editNickname(HttpServletRequest request, @PathVariable String nickname, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        String user = request.getRemoteUser();
        return ResponseEntity.ok(profileService.editNickname(user, nickname));
    }

    @Operation(summary = "마이페이지 프로필 캐릭터 조회", description = "캐릭터 꾸미기 전 프로필 정보 조회 메서드입니다.")
    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getMyProfile(HttpServletRequest request){
        try {
            String userEmail = request.getRemoteUser();
            ProfileDto profileDto = mypageService.getMyProfile(userEmail);
            return ResponseEntity.ok(profileDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "프로필 캐릭터 수정", description = "프로필 얼굴, 색깔, 아이템 수정 메서드입니다.")
    @PutMapping("/profile")
    public ResponseEntity<Member> editProfile(HttpServletRequest request,
                                              @RequestParam int profileFace, @RequestParam int profileColor) {
        String user = request.getRemoteUser();
        return ResponseEntity.ok(profileService.editProfile(user, profileFace, profileColor));
    }
}
