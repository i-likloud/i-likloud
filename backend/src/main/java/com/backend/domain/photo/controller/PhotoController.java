package com.backend.domain.photo.controller;

import com.backend.domain.photo.dto.PhotoResponseDto;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.service.PhotoService;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final PhotoService photoService;

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
