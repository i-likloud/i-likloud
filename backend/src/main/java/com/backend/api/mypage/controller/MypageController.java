package com.backend.api.mypage.controller;

import com.backend.api.mypage.dto.MypageInfoDto;
import com.backend.api.mypage.dto.ProfileDto;
import com.backend.domain.likes.entity.Likes;
import com.backend.domain.likes.repository.LikesRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.api.mypage.service.MypageService;
import com.backend.domain.member.service.MemberService;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Tag(name = "Mypage", description = "마이페이지 관련 api")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final MemberService memberService;


    @Operation(summary = "마이페이지 홈 조회", description = "마이페이지 홈의 조회 메서드입니다.")
    @GetMapping("/home")
    public ResponseEntity<MypageInfoDto> getMyInfo(@MemberInfo MemberInfoDto memberInfoDto){
        try {
            String email = memberInfoDto.getEmail();
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
    public ResponseEntity<MypageInfoDto> editNickname(@RequestParam String nickname, @MemberInfo MemberInfoDto memberInfoDto) {
        // 닉네임 중복 검사
        Optional<Member> findByNickname = memberRepository.findByNickname(nickname);
        if (findByNickname.isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED_NICKNAME);
        }
        String email = memberInfoDto.getEmail();
        MypageInfoDto mypageInfoDto = mypageService.editNickname(email,nickname);
        return ResponseEntity.ok(mypageInfoDto);
    }

    @Operation(summary = "마이페이지 프로필 캐릭터 조회", description = "캐릭터 꾸미기 전 프로필 정보 조회 메서드입니다.")
    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getMyProfile(@MemberInfo MemberInfoDto memberInfoDto){
        try {
            String email = memberInfoDto.getEmail();
            ProfileDto profileDto = mypageService.getMyProfile(email);
            return ResponseEntity.ok(profileDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "프로필 캐릭터 수정", description = "프로필 얼굴, 색깔, 아이템 수정 메서드입니다.")
    @PutMapping("/profile")
    public ResponseEntity<ProfileDto> editProfile(@RequestBody ProfileDto.editRequest request, @MemberInfo MemberInfoDto memberInfoDto) {
        String email = memberInfoDto.getEmail();
        ProfileDto profileDto = mypageService.editProfile(email,request);
        return ResponseEntity.ok(profileDto);
    }

    @Operation(summary = "나의 좋아요 그림 조회", description = "유저가 좋아요한 그림 리스트를 출력하는 메서드입니다.")
    @GetMapping("/likes")
    public ResponseEntity<List<Likes>> getMyLikes(@MemberInfo MemberInfoDto memberInfoDto){
        try {
            String email = memberInfoDto.getEmail();
            MypageInfoDto myPageInfoDto = mypageService.getMyInfo(email);
            Long memberId = myPageInfoDto.getMemberId();
            List<Likes> likes = memberService.getLikesByMemberId(memberId);
            return ResponseEntity.ok(likes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "내가 그린 그림 조회", description = "유저가 그린 그림 리스트를 출력하는 메서드입니다.")
    @GetMapping("/drawings")
    public ResponseEntity<List<Likes>> getMyDrawings(@MemberInfo MemberInfoDto memberInfoDto){
        try {
            String email = memberInfoDto.getEmail();
            MypageInfoDto myPageInfoDto = mypageService.getMyInfo(email);
            Long memberId = myPageInfoDto.getMemberId();
            List<Likes> likes = memberService.getDrawingsByMemberId(memberId);
            return ResponseEntity.ok(likes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}