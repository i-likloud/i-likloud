package com.backend.api.photo.controller;

import com.backend.api.photo.dto.PhotoInfoResponseDto;
import com.backend.api.photo.dto.PhotoWithDrawingsResponseDto;
import com.backend.api.photo.service.PhotoInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/photo")
public class PhotoInfoController {

    private final PhotoInfoService photoInfoService;

    //전체 조회(북마크 순, 최신순)
    @GetMapping("/new")
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllDesc() {
        return photoInfoService.searchAllDesc();
    }

    //전체 조회(그림그린순)
    @GetMapping("/pick")
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllPickCntDesc() {
        return photoInfoService.searchAllPickCntDesc();
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
}

