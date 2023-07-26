package com.backend.domain.member.controller;

import com.backend.domain.member.dto.MypageInfoDto;
import com.backend.domain.member.dto.ProfileDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MypageService;
import com.backend.domain.member.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Tag(name = "Mypage", description = "마이페이지 관련 api")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;
    private final ProfileService profileService;


    @Operation(summary = "마이페이지 홈 조회", description = "마이페이지 홈의 조회 메서드입니다.")
    @GetMapping("/home") //마이페이지 조회
    public ResponseEntity<MypageInfoDto> getMyInfo(HttpServletRequest request){
        try {
            String userEmail = request.getRemoteUser(); // 현재 사용자 조회
            MypageInfoDto myPageInfoDto = mypageService.getMyInfo(userEmail);
            return ResponseEntity.ok(myPageInfoDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "닉네임 수정", description = "프로필의 닉네임 수정 메서드입니다.")
    @PutMapping("/nickname")
    public ResponseEntity<Member> editNickname(HttpServletRequest request, @RequestParam String nickname) {
        String user = request.getRemoteUser();
        return ResponseEntity.ok(profileService.editNickname(user, nickname));
    }

    @Operation(summary = "마이페이지 프로필 캐릭터 조회", description = "캐릭터 꾸미기 전 프로필 정보 조회 메서드입니다.")
    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getMyProfile(HttpServletRequest request){
        try {
            String userEmail = request.getRemoteUser(); // 현재 사용자 조회
            ProfileDto profileDto = mypageService.getMyProfile(userEmail);
            return ResponseEntity.ok(profileDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
