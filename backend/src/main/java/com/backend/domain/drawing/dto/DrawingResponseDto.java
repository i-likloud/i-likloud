package com.backend.domain.drawing.dto;

import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingResponseDto {

    private final Long drawingId;
    private final String title;
    private final String artist;
    private final Long memberId;
    private final String imageUrl;

    public DrawingResponseDto(Drawing drawing) {
        this.drawingId = drawing.getDrawingId();
        this.title = drawing.getTitle();
        this.artist = drawing.getArtist();
        this.memberId = drawing.getMember().getMemberId();
        this.imageUrl = drawing.getImageUrl();
    }

}
