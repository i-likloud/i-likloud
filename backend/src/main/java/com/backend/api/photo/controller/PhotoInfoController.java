package com.backend.api.photo.controller;

import com.backend.api.photo.dto.PhotoInfoResponseDto;
import com.backend.api.photo.dto.PhotoWithDrawingsResponseDto;
import com.backend.api.photo.service.PhotoInfoService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/photo")
public class PhotoInfoController {

    private final PhotoInfoService photoInfoService;
    private final MemberService memberService;

    //전체 조회(최신순)
    @GetMapping("/new")
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllDesc() {
        return photoInfoService.searchAllDesc();
    }

    //전체 조회(그림그린순)
    @GetMapping("/pick")
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllPickCntDesc() {
        return photoInfoService.searchAllPickCntDesc();
    }

    //전체 조회(북마크 순)
    @GetMapping("/bookmarkdesc")
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllBookmarkCntDesc() {
        return photoInfoService.searchAllBookmarkCntDesc();
    }


        // 클릭한 photoId와 관련된 모든 drawings 조회
    @GetMapping("/{photoId}/alldrawings")
    public ResponseEntity<PhotoWithDrawingsResponseDto> getDrawingsByPhotoId(@PathVariable Long photoId) {
        return photoInfoService.searchDrawingsByPhotoId(photoId);
    }

    // 삭제
    @DeleteMapping("/delete/{id}")
    public void photoDelete(@PathVariable Long id){
        photoInfoService.delete(id);
    }

    // 사진 즐겨찾기
    @PostMapping("/{photoId}/pick")
    public ResponseEntity<String> pickPhoto(@PathVariable Long photoId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();
        return photoInfoService.pickPhoto(photoId, memberId);
    }

    // 사진 즐겨찾기 취소
    @PostMapping("/{photoId}/unpick")
    public ResponseEntity<String> unpickPhoto(@PathVariable Long photoId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();
        return photoInfoService.unpickPhoto(photoId, memberId);
    }

    // pickCnt 증가
    @PostMapping("/{photoId}/plus")
    public ResponseEntity<String> handlePhotoClick(@PathVariable Long photoId){
        photoInfoService.handlePhotoClick(photoId);
        return ResponseEntity.ok("사진의 pickCnt 증가가 정상적으로 완료돼었습니다.");
    }
}

