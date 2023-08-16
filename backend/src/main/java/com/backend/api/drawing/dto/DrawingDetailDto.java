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

    private final String title;
    private final String content;
    private final String artist;
    private final boolean nftYn;
    private final boolean memberLiked;
    private final int likesCount;
    private final int viewCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    private final List<CommentDto> comments;

    public DrawingDetailDto(Drawing drawing, boolean memberLiked) {
        super(drawing);
        this.title = drawing.getTitle();
        this.artist = drawing.getArtist();
        this.content = drawing.getContent();
        this.nftYn = drawing.isNftYn();
        this.memberLiked = memberLiked;
        this.likesCount = drawing.getLikesCount();
        this.viewCount = drawing.getViewCount();
        this.createdAt = drawing.getCreatedAt();
        this.comments = drawing.getComments().stream().map(CommentDto::new).collect(Collectors.toList());

    }
}
