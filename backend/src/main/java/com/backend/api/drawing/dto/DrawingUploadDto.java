package com.backend.api.drawing.dto;

import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingUploadDto {

    private final String title;
    private final String artist;
    private final String content;
    private final String imageUrl;

    public DrawingUploadDto(Drawing drawing) {
        this.title = drawing.getTitle();
        this.artist = drawing.getArtist();
        this.content = drawing.getContent();
        this.imageUrl = drawing.getImageUrl();
    }
}
