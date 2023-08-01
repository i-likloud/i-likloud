package com.backend.api.vision.controller;

import com.backend.api.vision.dto.VisionResponseDto;
import com.backend.api.vision.dto.VisionSafeResponseDto;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import com.google.cloud.spring.vision.CloudVisionTemplate;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Vision", description = "구글 비전 api")
@RestController
@RequestMapping("/api/vision")
@RequiredArgsConstructor
public class VisionController {

    private final CloudVisionTemplate cloudVisionTemplate;

    @PostMapping(value = "/checkPhoto",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "사진 판별(테스트용)", description = "하늘, 구름사진인지 판별합니다.")
    public ResponseEntity<List<VisionResponseDto>> extractLabels(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_FILE);
        }
        try {
            Resource imageResource = new ByteArrayResource(file.getBytes());
            AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(imageResource, Type.LABEL_DETECTION);

            List<EntityAnnotation> annotations = response.getLabelAnnotationsList();
            List<VisionResponseDto> annotationDtos = annotations.stream()
                    .map(a -> new VisionResponseDto(a.getDescription(), a.getScore()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(annotationDtos);
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 읽는 도중 오류가 발생했습니다.", e);
        }
    }

    @PostMapping(value = "/detectSafeSearch",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유해성 검사(테스트용)", description = "사진의 유해성 검사를 합니다.")
    public ResponseEntity<VisionSafeResponseDto> detectSafeSearch(@RequestParam("file") MultipartFile file) throws IOException {

        try {
            Resource imageResource = new ByteArrayResource(file.getBytes());
            AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(imageResource, Type.SAFE_SEARCH_DETECTION);

            SafeSearchAnnotation annotation = response.getSafeSearchAnnotation();
            VisionSafeResponseDto annotationDto = new VisionSafeResponseDto(annotation.getRacy(), annotation.getMedical(), annotation.getSpoof(), annotation.getAdult(), annotation.getViolence());

            return ResponseEntity.ok(annotationDto);
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 읽는 도중 오류가 발생했습니다.", e);
        }

        // API 요청 직접 생성하는 방법
//        List<AnnotateImageRequest> requests = new ArrayList<>();
//
//        ByteString imgBytes = ByteString.copyFrom(file.getBytes());
//
//        Image img = Image.newBuilder().setContent(imgBytes).build();
//        Feature feat = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
//        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
//        requests.add(request);
//
//        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
//            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
//            List<AnnotateImageResponse> responses = response.getResponsesList();
//
//            Map<String, Object> result = new HashMap<>();
//
//            for (AnnotateImageResponse res : responses) {
//                if (res.hasError()) {
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res.getError().getMessage());
//                }
//
//                SafeSearchAnnotation annotation = res.getSafeSearchAnnotation();
//
//                result.put("adult", annotation.getAdult());
//                result.put("medical", annotation.getMedical());
//                result.put("spoofed", annotation.getSpoof());
//                result.put("violence", annotation.getViolence());
//                result.put("racy", annotation.getRacy());
//            }
//            return ResponseEntity.ok().body(result);
//        }
    }
}
