package com.backend.domain.photo.controller;

import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.photo.dto.PhotoUploadDto;
import com.backend.domain.photo.service.PhotoService;
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

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Photo", description = "사진 관련 api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/photo")
public class PhotoController {
    private final PhotoService photoService;
    private final MemberService memberService;

    @Operation(summary = "사진 등록", description = "사진들을 등록할 수 있는 메소드입니다. 여러개 한번에 업로드 가능합니다.")
    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPhotos(@RequestPart List<MultipartFile> multipartFiles, @MemberInfo MemberInfoDto memberInfoDto){
        Member member = memberService.findMemberByEmail((memberInfoDto.getEmail()));
        List<PhotoUploadDto> uploadPhotos = new ArrayList<>();
        try{
            for (MultipartFile file : multipartFiles){
                PhotoUploadDto photo = photoService.saveFileAndCreatePhotos(file, member);
                uploadPhotos.add(photo);
                }
            return ResponseEntity.ok().body(uploadPhotos);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
