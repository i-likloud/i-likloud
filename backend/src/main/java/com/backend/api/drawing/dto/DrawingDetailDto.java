package com.backend.api.drawing.dto;

import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingDetailDto {

    private final String title;
    private final String artist;
    private final String content;
    private final String imageUrl;
    private final boolean memberLiked;
    private final int viewCount;
    private final int likesCount;

    public DrawingDetailDto(Drawing drawing, boolean memberLiked) {
        this.title = drawing.getTitle();
        this.artist = drawing.getArtist();
        this.content = drawing.getContent();
        this.imageUrl = drawing.getImageUrl();
        this.viewCount = drawing.getViewCount();
        this.likesCount = drawing.getLikesCount();
        this.memberLiked = memberLiked;
    }
}
