package com.backend.api.drawing.dto;

import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingUploadDto extends DrawingListDto {

    private final String title;
    private final String content;
    private final Long photoId;


    public DrawingUploadDto(Drawing drawing) {
        super(drawing);
        this.title = drawing.getTitle();
        this.content = drawing.getContent();
        this.photoId = drawing.getPhoto().getPhotoId();
    }
}
