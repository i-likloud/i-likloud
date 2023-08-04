package com.backend.api.comment.service;

import com.backend.domain.comment.dto.CommentDto;
import com.backend.domain.comment.entity.Comment;
import com.backend.domain.comment.repository.CommentRepository;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.entity.Member;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final DrawingRepository drawingRepository;
    private final CommentRepository commentRepository;

    // 댓글 생성
    @Transactional
    public CommentDto createComment(Long drawingId, String content, Member member) {

        Drawing drawing = drawingRepository.findById(drawingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DRAWING));

        Comment comment = Comment.builder()
                .content(content)
                .drawing(drawing)
                .member(member)
                .commentMember(member.getNickname())
                .build();

        commentRepository.save(comment);

        return new CommentDto(comment);

    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_COMMENT));

        if(!comment.getMember().getMemberId().equals(memberId)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
        commentRepository.delete(comment);
    }
}
