package com.backend.domain.photo.controller;

import com.backend.domain.photo.dto.PhotoResponseDto;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.service.PhotoService;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Photo", description = "사진 관련 api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/photo")
public class PhotoController {
    private final PhotoService photoService;

    @Operation(summary = "사진 등록", description = "사진을 등록할 수 있는 메소드입니다.")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(@RequestPart MultipartFile file, Photo photoRequest, @MemberInfo MemberInfoDto memberInfoDto){
        try{
            PhotoResponseDto photo = photoService.saveFileAndCreatePhotos(file, photoRequest, memberInfoDto);
            return ResponseEntity.ok().body(photo);
        }catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
