package com.backend.domain.photo.controller;

import com.backend.api.vision.dto.VisionResponseDto;
import com.backend.api.vision.dto.VisionSafeResponseDto;
import com.backend.api.vision.service.VisionService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.photo.dto.PhotoFailedDto;
import com.backend.domain.photo.dto.PhotoUploadDto;
import com.backend.domain.photo.entity.PhotoFile;
import com.backend.domain.photo.service.PhotoService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Photo", description = "사진 관련 api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/photo")
public class PhotoController {

    private final PhotoService photoService;
    private final MemberService memberService;
    private final VisionService visionService;

//    @Operation(summary = "사진 등록", description = "사진들을 등록할 수 있는 메소드입니다. 여러개 한번에 업로드 가능합니다.")
//    @PostMapping(value = "/upload",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> uploadPhotos(@RequestPart List<MultipartFile> multipartFiles, @MemberInfo MemberInfoDto memberInfoDto){
//        Member member = memberService.findMemberByEmail((memberInfoDto.getEmail()));
//        List<PhotoUploadDto> uploadPhotos = new ArrayList<>();
//        try{
//            for (MultipartFile file : multipartFiles){
//                PhotoUploadDto photo = photoService.saveFileAndCreatePhotos(file, member);
//                uploadPhotos.add(photo);
//                }
//            return ResponseEntity.ok().body(uploadPhotos);
//        } catch (BusinessException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @Operation(summary = "사진 등록", description = "사진들을 등록할 수 있는 메소드입니다. 여러개 한번에 업로드 가능합니다.\n\n " +
            "업로드 전 객체 확인, 유해성 검사 진행 후 패스되면 업로드 진행\n\n" +
            "검사 둘 중 하나라도 안되면 업로드 하지않고 해당 사진 url 반환")
    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPhotos(@RequestPart List<MultipartFile> multipartFiles, @MemberInfo MemberInfoDto memberInfoDto) throws IOException {
        Member member = memberService.findMemberByEmail((memberInfoDto.getEmail()));
        List<PhotoUploadDto> uploadPhotos = new ArrayList<>();

        // 사진들이 적합한지 판별 진행
        for (MultipartFile file : multipartFiles){
            // 라벨 추출, 유해성 검사 진행
            List<VisionResponseDto> annotations = visionService.analyzePhoto(file);
            ResponseEntity<VisionSafeResponseDto> visionSafeResponseDto = visionService.detectSafeSearch(file);
            String photoType = "cloud";
            String message = "success";
            // 유해한 사진인 경우
            if (!visionService.isSafePhoto(visionSafeResponseDto)) {
                photoType = "danger";
                message = "유해한 이미지입니다.";
            }
            // 하늘, 구름이 아닌경우
             else if (!visionService.isValidPhoto(annotations)) {
                photoType = "noCloud";
                message = "이미지가 구름 또는 하늘 사진이 아닙니다.";
            }
            // 적합한 사진이 아니면 그 사진 업로드해놓고 URL 반환
            if (!photoType.equals("cloud")) {
                try {
                    PhotoFile failedFile = photoService.uploadPhotoFile(file, photoType);
                    PhotoFailedDto photoFailedDto = new PhotoFailedDto(failedFile.getFilePath(), message);
                    return ResponseEntity.badRequest().body(photoFailedDto);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // 모든 사진들이 적합하면 업로드를 진행
        for (MultipartFile file : multipartFiles){
            PhotoUploadDto photo = photoService.saveFileAndCreatePhotos(file, member);
            uploadPhotos.add(photo);
        }
        return ResponseEntity.ok().body(uploadPhotos);
    }

}
