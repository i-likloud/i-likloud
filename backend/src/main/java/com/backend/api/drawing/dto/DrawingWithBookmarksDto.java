package com.backend.api.drawing.dto;

import com.backend.domain.drawing.dto.DrawingResponseDto;
import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingWithBookmarksDto extends DrawingResponseDto {
    private final String title;
    private final String artist;
    private final int viewCount;
    private final int likesCount;
    private final boolean memberLiked;


    public DrawingWithBookmarksDto(Drawing drawing, boolean memberLiked) {
        super(drawing);
        this.title = drawing.getTitle();
        this.artist = drawing.getArtist();
        this.viewCount = drawing.getViewCount();
        this.likesCount = drawing.getLikesCount();
        this.memberLiked = memberLiked;

    }
}
