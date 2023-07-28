package com.backend.api.drawing.controller;

import com.backend.api.drawing.dto.DrawingDetailDto;
import com.backend.api.drawing.dto.DrawingListDto;
import com.backend.api.drawing.dto.DrawingUploadDto;
import com.backend.api.drawing.service.DrawingViewService;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.api.drawing.service.DrawingUploadService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Drawing", description = "그림 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drawings")
public class DrawingController {

    private final DrawingUploadService drawingUploadService;
    private final DrawingViewService drawingViewService;
    private final MemberService memberService;

    @PostMapping("/upload/from/{photoId}")
    @Operation(summary = "그림 업로드", description = "그림 파일, 제목, 내용을 요청으로 보내서 그림을 업로드합니다.")


    public ResponseEntity<?> uploadDrawing(@RequestPart(value = "file") MultipartFile file,
                                           @RequestPart(value = "title") String title,
                                           @RequestPart(value = "content") String content, @MemberInfo MemberInfoDto memberInfoDto, @PathVariable Long photoId) {
        try {
            DrawingUploadDto uploadDrawing = drawingUploadService.uploadFileAndCreateDrawings(file, title, content, memberInfoDto, photoId);
            return ResponseEntity.ok().body(uploadDrawing);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @Operation(summary = "그림 게시물 전체 조회", description = "그림 게시물들을 전체 조회합니다. \n 기본값은 최신순 파라미터로 ?orderBy=likesCount면 좋아요 \n ?orderBy=viewCount면 조회수순")
    public ResponseEntity<List<DrawingListDto>> drawingBoard(@MemberInfo MemberInfoDto memberInfoDto, @RequestParam(defaultValue = "createdAt") String orderBy){
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        List<DrawingListDto> drawings = drawingViewService.getAllDrawings(member.getMemberId(), orderBy);
        return ResponseEntity.ok().body(drawings);
    }

    @GetMapping("/{drawingId}")
    @Operation(summary = "그림 게시물 상세 조회", description = "그림 게시물을 상세 조회합니다.")
    public ResponseEntity<DrawingDetailDto> getDrawing(@PathVariable Long drawingId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        DrawingDetailDto drawing = drawingViewService.getDrawing(drawingId, member.getMemberId());
        return ResponseEntity.ok(drawing);
    }

    @DeleteMapping("/delete/{drawingId}")
    @Operation(summary = "그림 게시물 삭제", description = "그림 게시물을 삭제합니다. 본인이 작성한 게시물만 삭제할 수 있습니다.")
    public ResponseEntity<String> deleteDrawing(@PathVariable Long drawingId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        drawingViewService.deleteDrawing(drawingId, member.getMemberId());
        return ResponseEntity.ok(String.format("%d번 게시물 삭제ㅠㅠ", drawingId));
    }
}
