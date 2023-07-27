package com.backend.api.drawing.dto;

import com.backend.domain.comment.dto.CommentDto;
import com.backend.domain.drawing.entity.Drawing;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DrawingDetailDto extends DrawingListDto {

    private final String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    private final List<CommentDto> comments;

    public DrawingDetailDto(Drawing drawing, boolean memberLiked) {
        super(drawing, memberLiked);
        this.content = drawing.getContent();
        this.createdAt = drawing.getCreatedAt();
        this.comments = drawing.getComments().stream().map(CommentDto::new).collect(Collectors.toList());

    }
}
