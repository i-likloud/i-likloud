package com.backend.api.drawing.dto;

import com.backend.domain.drawing.dto.DrawingResponseDto;
import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingUploadDto extends DrawingResponseDto {

    private final String content;


    public DrawingUploadDto(Drawing drawing) {
        super(drawing);
        this.content = drawing.getContent();
    }
}
