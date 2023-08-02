package com.backend.domain.comment.dto;

import com.backend.domain.comment.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {

    private final Long commentId;
    private final String content;
    private final String commentMember;
    private final Long drawingId;
    private final Long memberId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    public CommentDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.commentMember = comment.getCommentMember();
        this.createdAt = comment.getCreatedAt();
        this.memberId = comment.getMember().getMemberId();
        this.drawingId = comment.getDrawing().getDrawingId();
    }
}