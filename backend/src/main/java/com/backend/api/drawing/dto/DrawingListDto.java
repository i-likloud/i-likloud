package com.backend.api.drawing.dto;

import com.backend.domain.drawing.dto.DrawingResponseDto;
import com.backend.domain.drawing.entity.Drawing;
import lombok.Getter;

@Getter
public class DrawingListDto extends DrawingResponseDto {


    public DrawingListDto(Drawing drawing) {
        super(drawing);
    }

}
