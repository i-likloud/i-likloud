package com.backend.api.drawing.dto;

import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingWithBookmarksDto {
    private Long drawingId;
    private String title;
    private String artist;
    private String imageUrl;
    private int viewCount;
    private int likesCount;

    public DrawingWithBookmarksDto() {
    }

    public DrawingWithBookmarksDto(Drawing drawing) {
        this.drawingId = drawing.getDrawingId();
        this.title = drawing.getTitle();
        this.artist = drawing.getArtist();
        this.imageUrl = drawing.getImageUrl();
        this.viewCount = drawing.getViewCount();
        this.likesCount = drawing.getLikesCount();
    }
}
