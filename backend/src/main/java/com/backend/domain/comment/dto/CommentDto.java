package com.backend.domain.comment.dto;

import com.backend.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {

    private final Long commentId;
    private final String content;
    private final String commentMember;
    private final Long drawingId;
    private final Long memberId;
    private final String nickname;
    private final int profileFace;
    private final int profileColor;
    private final int profileAccessory;

    private final LocalDateTime createdAt;

    public CommentDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.commentMember = comment.getCommentMember();
        this.createdAt = comment.getCreatedAt();
        this.memberId = comment.getMember().getMemberId();
        this.nickname = comment.getMember().getNickname();
        this.profileFace = comment.getMember().getProfileFace();
        this.profileColor = comment.getMember().getProfileColor();
        this.profileAccessory = comment.getMember().getProfileAccessory();
        this.drawingId = comment.getDrawing().getDrawingId();
    }
}