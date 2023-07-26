package com.backend.api.photo.dto;

import com.backend.domain.drawing.dto.DrawingResponseDto;
import com.backend.domain.photo.entity.Photo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PhotoWithDrawingsResponseDto {

    private Long photoId;
    private String photoUrl;
    private int pickCnt;
    private List<DrawingResponseDto> drawings;

    public PhotoWithDrawingsResponseDto(Photo photo) {
        this.photoId = photo.getPhotoId();
        this.photoUrl = photo.getPhotoUrl();
        this.pickCnt = photo.getPickCnt();
        this.drawings = photo.getDrawings().stream()
                .map(DrawingResponseDto::new)
                .collect(Collectors.toList());
    }
}
