package com.backend.api.photo.controller;

import com.backend.api.photo.dto.PhotoInfoResponseDto;
import com.backend.api.photo.dto.PhotoWithDrawingsResponseDto;
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
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllDesc() {
        return photoInfoService.searchAllDesc();
    }

    //전체 조회(그림그린순)
    @Operation(summary = "전체 조회(그림 많이 그린 순)", description = "그림 많이 그린 순으로 정렬된 전체 조회 메소드입니다.")
    @GetMapping("/pick")
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllPickCntDesc() {
        return photoInfoService.searchAllPickCntDesc();
    }

    //전체 조회(북마크 순)
    @Operation(summary = "전체 조회(즐겨찾기순)", description = "즐겨찾기순으로 정렬된 전체 조회 메소드입니다.")
    @GetMapping("/bookmarkdesc")
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllBookmarkCntDesc() {
        return photoInfoService.searchAllBookmarkCntDesc();
    }


        // 클릭한 photoId와 관련된 모든 drawings 조회
        @Operation(summary = "사진 관련 모든 그림", description = "특정 사진과 관련된 모든 그림을 출력하는 메소드입니다.")
        @GetMapping("/{photoId}/alldrawings")
    public ResponseEntity<PhotoWithDrawingsResponseDto> getDrawingsByPhotoId(@PathVariable Long photoId) {
        return photoInfoService.searchDrawingsByPhotoId(photoId);
    }

    // 삭제
    @Operation(summary = "사진 삭제", description = "사진을 삭제하는 메소드입니다.")
    @DeleteMapping("/delete/{id}")
    public void photoDelete(@PathVariable Long id){
        photoInfoService.delete(id);
    }

    // 사진 즐겨찾기
    @Operation(summary = "즐겨찾기 추가", description = "사진을 즐겨찾기 할 수 있는 메소드입니다.")
    @PostMapping("/{photoId}/pick")
    public ResponseEntity<String> pickPhoto(@PathVariable Long photoId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();
        return photoInfoService.pickPhoto(photoId, memberId);
    }

    // 사진 즐겨찾기 취소
    @Operation(summary = "즐겨찾기 제거", description = "즐겨찾기한 사진을 해제 할 수 있는 메소드입니다.")
    @PostMapping("/{photoId}/unpick")
    public ResponseEntity<String> unpickPhoto(@PathVariable Long photoId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();
        return photoInfoService.unpickPhoto(photoId, memberId);
    }

    // pickCnt 증가
    @Operation(summary = "그림 그리면 사진의 인기도 상승", description = "그림을 그리면 그 기반의 사진의 인기도를 상승시키는 메소드입니다.")
    @PostMapping("/{photoId}/plus")
    public ResponseEntity<String> handlePhotoClick(@PathVariable Long photoId){
        photoInfoService.handlePhotoClick(photoId);
        return ResponseEntity.ok("사진의 pickCnt 증가가 정상적으로 완료돼었습니다.");
    }
}

