package com.backend.api.photo.controller;

import com.backend.api.drawing.dto.DrawingWithBookmarksDto;
import com.backend.api.photo.dto.PhotoDetailDto;
import com.backend.api.photo.dto.PhotoInfoResponseDto;
import com.backend.api.photo.service.PhotoInfoService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Photo", description = "사진 관련 api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/photo")
public class PhotoInfoController {

    private final PhotoInfoService photoInfoService;
    private final MemberService memberService;


    //전체 조회(최신순)
    @Operation(summary = "전체 조회(최신순)", description = "최신순으로 정렬된 전체 조회 메소드입니다.")
    @GetMapping("/new")
    public List<PhotoInfoResponseDto> searchAllDesc(@MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        return photoInfoService.searchAllDesc(member);
    }

    //전체 조회(그림그린순)
    @Operation(summary = "전체 조회(그림 많이 그린 순)", description = "그림 많이 그린 순으로 정렬된 전체 조회 메소드입니다.")
    @GetMapping("/pick")
    public List<PhotoInfoResponseDto> searchAllPickCntDesc(@MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        return photoInfoService.searchAllPickCntDesc(member);
    }

    //전체 조회(북마크 순)
    @Operation(summary = "전체 조회(즐겨찾기순)", description = "즐겨찾기순으로 정렬된 전체 조회 메소드입니다.")
    @GetMapping("/bookmarkdesc")
    public List<PhotoInfoResponseDto> searchAllBookmarkCntDesc(@MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        return photoInfoService.searchAllBookmarkCntDesc(member);
    }

    //사진 상세 조회
    @Operation(summary = "사진 상세 조회", description = "사진 상세조회(url, 사진id, 멤버id).")
    @GetMapping("/{photoId}")
    public PhotoDetailDto photoDetail(@PathVariable Long photoId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        return photoInfoService.getPhotoDetail(photoId, member);
    }

    // 클릭한 photoId와 관련된 모든 drawings 조회
    @Operation(summary = "사진 관련 모든 그림", description = "특정 사진과 관련된 모든 그림을 출력하는 메소드입니다.")
    @GetMapping("/{photoId}/alldrawings")
    public ResponseEntity<List<DrawingWithBookmarksDto>> getDrawingsByPhotoId(@PathVariable Long photoId) {
        List<DrawingWithBookmarksDto> drawings = photoInfoService.getDrawingsByPhotoId(photoId);
        return ResponseEntity.ok(drawings);
    }

    // 삭제
    @Operation(summary = "사진 삭제", description = "사진을 삭제하는 메소드입니다.")
    @DeleteMapping("/delete/{photoId}")
    public ResponseEntity<String> photoDelete(@PathVariable Long photoId, @MemberInfo MemberInfoDto memberInfoDto){
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        photoInfoService.delete(photoId, member);

        return ResponseEntity.ok(String.format("%d번 사진 삭제ㅠㅠ", photoId));
    }

    // 사진 즐겨찾기 토글
    @Operation(summary = "즐겨찾기 추가", description = "사진을 즐겨찾기 토글을 할 수 있는 메소드입니다.")
    @PostMapping("/{photoId}/bookmark")
    public ResponseEntity<String> pickPhoto(@PathVariable Long photoId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        if(photoInfoService.isAlreadyBookmarked(member, photoId)) {
            photoInfoService.unpickPhoto(photoId, member.getMemberId());
            return  ResponseEntity.ok(String.format("%d번 사진 북마크 취소", photoId));
        }
        else {
            photoInfoService.pickPhoto(photoId, member.getMemberId());
            return  ResponseEntity.ok(String.format("%d번 사진 북마크", photoId));
        }
    }

}

