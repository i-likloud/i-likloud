package com.backend.api.photo.controller;

import com.backend.api.photo.dto.PhotoInfoResponseDto;
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

    //전체 조회(즐겨찾기 순, 북마크 순, 최신순)
    @GetMapping("/new")
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllDesc() {
        return photoInfoService.searchAllDesc();
    }

    // 삭제
    @DeleteMapping("/delete/{id}")
    public void photoDelete(@PathVariable Long id){
        photoInfoService.delete(id);
    }
}

