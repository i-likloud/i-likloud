package com.backend.api.mypage.controller;

import com.backend.api.drawing.dto.DrawingListDto;
import com.backend.api.mypage.dto.MypageInfoDto;
import com.backend.api.mypage.dto.ProfileDto;
import com.backend.api.mypage.service.MypageService;
import com.backend.api.photo.dto.PhotoWithBookmarkDto;
import com.backend.domain.accessory.dto.AccessoryDto;
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
    private final MemberService memberService;


    @Operation(summary = "마이페이지 홈 조회", description = "마이페이지 홈의 조회 메서드입니다.")
    @GetMapping("/home")
    public ResponseEntity<MypageInfoDto> getMyInfo(@MemberInfo MemberInfoDto memberInfoDto) {
        try {
            String email = memberInfoDto.getEmail();
            if (email == null) {
                throw new BusinessException(ErrorCode.MEMBER_NOT_EXISTS);
            }
            MypageInfoDto myPageInfoDto = mypageService.getMyInfo(email);
            return ResponseEntity.ok(myPageInfoDto);
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
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        MypageInfoDto mypageInfoDto = mypageService.editNickname(member,nickname);
        return ResponseEntity.ok(mypageInfoDto);
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
    public ResponseEntity<List<DrawingListDto>> getMyLikes(@MemberInfo MemberInfoDto memberInfoDto){
        try {
            Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
            List<DrawingListDto> result = mypageService.likeDrawing(member.getMemberId());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "내가 그린 그림 조회", description = "유저가 그린 그림 리스트를 출력하는 메서드입니다.")
    @GetMapping("/drawings")
    public ResponseEntity<List<DrawingListDto>> getMyDrawings(@MemberInfo MemberInfoDto memberInfoDto) {
        try {
            Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
            List<DrawingListDto> result = mypageService.getMyDrawing(member.getMemberId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(summary = "나의 북마크 사진 조회", description = "유저가 북마크한 사진 리스트를 출력하는 메서드입니다.")
    @GetMapping("/bookmarks")
    public ResponseEntity<List<PhotoWithBookmarkDto>> getMyBookmarks(@MemberInfo MemberInfoDto memberInfoDto){
        try {
            Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
            List<PhotoWithBookmarkDto> result = mypageService.bookmarkPhoto(member.getMemberId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "내가 올린 사진 조회", description = "유저가 올린 사진 리스트를 출력하는 메서드입니다.")
    @GetMapping("/photos")
    public ResponseEntity<List<PhotoWithBookmarkDto>> getMyPhotos(@MemberInfo MemberInfoDto memberInfoDto){
        try {
            Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
            List<PhotoWithBookmarkDto> result = mypageService.getMyPhoto(member.getMemberId());
//            List<Photo> result = photoRepository.findAllByMemberMemberId(member.getMemberId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "내가 가진 아이템 조회", description = "본인이 가진 아이템 목록을 출력하는 메서드입니다.")
    @GetMapping("/accessory")
    public ResponseEntity<List<AccessoryDto>> getMyAccessorys (@MemberInfo MemberInfoDto memberInfoDto){
        try{
            Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
            List<AccessoryDto> result = mypageService.getMyAccessory(member.getMemberId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


//    @Operation(summary = "내가 즐겨찾기한 사진 조회", description = "즐겨찾기한 모든 사진을 보여주는 메소드입니다.")
//    @GetMapping("/bookmarks")
//    public ResponseEntity<List<PhotoWithBookmarkDto>> getBookmarkedPhotosForCurrentUser(@MemberInfo MemberInfoDto memberInfoDto) {
//        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
//        List<PhotoWithBookmarkDto> bookmarkedPhotos = bookmarkService.getBookmarkedPhotosForUser(findMember);
//        return ResponseEntity.ok(bookmarkedPhotos);
//    }
}
