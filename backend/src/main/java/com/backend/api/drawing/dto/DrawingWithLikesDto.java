package com.backend.api.drawing.dto;

import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class DrawingWithLikesDto {
    private Long drawingId;
    private String title;
    private String artist;
    private String content;
    private String imageUrl;
    private int viewCount;
    private int likesCount;

    public DrawingWithLikesDto() {
    }

    public DrawingWithLikesDto(Drawing drawing) {
        this.drawingId = drawing.getDrawingId();
        this.title = drawing.getTitle();
        this.artist = drawing.getArtist();
        this.content = drawing.getContent();
        this.imageUrl = drawing.getImageUrl();
        this.viewCount = drawing.getViewCount();
        this.likesCount = drawing.getLikesCount();
    }
}
