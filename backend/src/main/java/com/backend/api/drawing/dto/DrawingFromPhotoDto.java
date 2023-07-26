package com.backend.api.drawing.dto;

import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingFromPhotoDto {

    private final String title;
    private final String artist;
    private final String imageUrl;

    public DrawingFromPhotoDto(Drawing drawing) {
        this.title = drawing.getTitle();
        this.artist = drawing.getArtist();
        this.imageUrl = drawing.getImageUrl();

    }
}
