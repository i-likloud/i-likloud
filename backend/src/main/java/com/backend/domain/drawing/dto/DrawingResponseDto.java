package com.backend.domain.drawing.dto;

import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingResponseDto {

    private final Long drawingId;
    private final Long memberId;
    private final String title;
    private final String artist;
    private final String imageUrl;

    public DrawingResponseDto(Drawing drawing) {
        this.drawingId = drawing.getDrawingId();
        this.memberId = drawing.getMember().getMemberId();
        this.title = drawing.getTitle();
        this.artist = drawing.getArtist();
        this.imageUrl = drawing.getImageUrl();
    }

}
