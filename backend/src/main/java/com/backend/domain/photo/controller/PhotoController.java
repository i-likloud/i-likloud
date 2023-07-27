package com.backend.domain.photo.controller;

import com.backend.api.photo.dto.PhotoDto;
import com.backend.domain.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping("/register")
    public Long register(@RequestBody PhotoDto requestDto) {
        return photoService.register(requestDto);
    }
}
