package com.backend.domain.drawing.dto;

import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingResponseDto {

    private String title;
    private String artist;
    private String content;
    private String imageUrl;

    public DrawingResponseDto(Drawing drawing) {
        this.title = drawing.getTitle();
        this.artist = drawing.getArtist();
        this.content = drawing.getContent();
        this.imageUrl = drawing.getImageUrl();
    }

}
