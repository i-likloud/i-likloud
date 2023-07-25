package com.backend.api.drawing.controller;

import com.backend.domain.drawing.dto.DrawingResponseDto;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.service.DrawingService;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Drawing", description = "그림 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drawings")
public class DrawingController {

    private final DrawingService drawingService;

    @PostMapping("/upload")
    @Operation(summary = "그림 업로드", description = "JWT토큰과 그림 파일, 제목, 내용을 요청으로 보내서 그림을 업로드합니다.")
    public ResponseEntity<?> uploadDrawing(@RequestPart MultipartFile file, Drawing drawingRequest, @MemberInfo MemberInfoDto memberInfoDto) {
        try {
            DrawingResponseDto drawing = drawingService.saveFileAndCreateDrawings(file, drawingRequest, memberInfoDto);
            return ResponseEntity.ok().body(drawing);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @Operation(summary = "그림 게시물 전체 조회", description = "그림 게시물들을 전체 조회합니다.")
    public ResponseEntity<List<DrawingResponseDto>> drawingBoard(){
        List<DrawingResponseDto> drawings = drawingService.getAllDrawings();
        return ResponseEntity.ok().body(drawings);
    }
}
