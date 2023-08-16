package com.backend.api.vision.service;

import com.backend.api.vision.dto.VisionResponseDto;
import com.backend.api.vision.dto.VisionSafeResponseDto;
import com.google.cloud.spring.vision.CloudVisionTemplate;
import com.google.cloud.vision.v1.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisionService {

    private final CloudVisionTemplate cloudVisionTemplate;

    // 사진 판별
    public List<VisionResponseDto> analyzePhoto(MultipartFile file) {

        try {
            // 사진 파일을 바이트배열로 바꾼 뒤 비전 API 통해서 라벨 추출
            Resource imageResource = new ByteArrayResource(file.getBytes());
            AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(imageResource, Feature.Type.LABEL_DETECTION);

            List<EntityAnnotation> annotations = response.getLabelAnnotationsList();
            return annotations.stream()
                    .map(a -> new VisionResponseDto(a.getDescription(), a.getScore()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 읽는 도중 오류가 발생했습니다.", e);
        }
    }

    // 구름 또는 하늘 사진인지 판별
    public boolean isValidPhoto(List<VisionResponseDto> annotations) {
        return annotations.stream()
                .anyMatch(a -> (a.getDescription().equals("Sky") || a.getDescription().equals("Cloud")) && a.getScore() >= 0.8);
    }

    // 유해성 검사
    public ResponseEntity<VisionSafeResponseDto> detectSafeSearch(MultipartFile file) throws IOException {

        try {
            Resource imageResource = new ByteArrayResource(file.getBytes());
            AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(imageResource, Feature.Type.SAFE_SEARCH_DETECTION);

            SafeSearchAnnotation annotation = response.getSafeSearchAnnotation();
            VisionSafeResponseDto annotationDto = new VisionSafeResponseDto(annotation.getRacy(), annotation.getMedical(), annotation.getSpoof(), annotation.getAdult(), annotation.getViolence());

            return ResponseEntity.ok(annotationDto);
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 읽는 도중 오류가 발생했습니다.", e);
        }
    }

    // 유해성 검사 분석
    public boolean isSafePhoto(ResponseEntity<VisionSafeResponseDto> responseEntity) {

        VisionSafeResponseDto visionSafeResponseDto = responseEntity.getBody();

        List<Likelihood> annotations = Arrays.asList(
                visionSafeResponseDto.getRacy(),
                visionSafeResponseDto.getMedical(),
                visionSafeResponseDto.getSpoofed(),
                visionSafeResponseDto.getAdult(),
                visionSafeResponseDto.getViolence()
        );

        return annotations.stream().noneMatch(annotation ->
                annotation == Likelihood.POSSIBLE || annotation == Likelihood.LIKELY || annotation == Likelihood.VERY_LIKELY);
    }
}
