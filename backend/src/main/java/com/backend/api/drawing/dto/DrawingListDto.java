package com.backend.api.drawing.dto;

import com.backend.domain.drawing.entity.Drawing;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DrawingListDto{

    private final Long drawingId;
    private final Long memberId;
    private final String imageUrl;
    private final int likesCount;
    private final int viewCount;

    public DrawingListDto(Drawing drawing) {
        this.drawingId = drawing.getDrawingId();
        this.memberId = drawing.getMember().getMemberId();
        this.imageUrl = drawing.getImageUrl();
        this.likesCount = drawing.getLikesCount();
        this.viewCount = drawing.getViewCount();
    }

    @JsonCreator
    public DrawingListDto(@JsonProperty("drawingId") Long drawingId, @JsonProperty("memberId") Long memberId, @JsonProperty("imageUrl") String imageUrl,
                          @JsonProperty("likesCount") int likesCount, @JsonProperty("viewCount") int viewCount) {
        this.drawingId = drawingId;
        this.memberId = memberId;
        this.imageUrl = imageUrl;
        this.likesCount = likesCount;
        this.viewCount = viewCount;
    }
}
