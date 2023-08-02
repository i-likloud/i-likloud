package com.backend.api.drawing.dto;

import com.backend.domain.drawing.dto.DrawingResponseDto;
import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingListDto extends DrawingResponseDto {

    private final int viewCount;
    private final int likesCount;
    private final boolean memberLiked;
    private final boolean nftYn;

    public DrawingListDto(Drawing drawing, boolean memberLiked) {
        super(drawing);
        this.viewCount = drawing.getViewCount();
        this.likesCount = drawing.getLikesCount();
        this.memberLiked = memberLiked;
        this.nftYn = drawing.isNftYn();
    }
}
