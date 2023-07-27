package com.backend.api.drawing.dto;

import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingListDto {

    private final String title;
    private final String artist;
    private final String imageUrl;
    private final int viewCount;
    private final int likesCount;
    private final boolean memberLiked;

    public DrawingListDto(Drawing drawing, boolean memberLiked) {
        this.title = drawing.getTitle();
        this.artist = drawing.getArtist();
        this.imageUrl = drawing.getImageUrl();
        this.viewCount = drawing.getViewCount();
        this.likesCount = drawing.getLikesCount();
        this.memberLiked = memberLiked;
    }
}
