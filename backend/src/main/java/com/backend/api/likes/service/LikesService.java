package com.backend.api.likes.service;

import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.likes.entity.Likes;
import com.backend.domain.likes.repository.LikesRepository;
import com.backend.domain.member.entity.Member;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikesService {

    private final LikesRepository likesRepository;

    private final DrawingRepository drawingRepository;

    // 좋아요
    @Transactional
    @CacheEvict(value = "likes", key = "#member.memberId")
    public void addLike(Member member, Long drawingId) {
        Drawing drawing = drawingRepository.findById(drawingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_FILE));

        Likes likes = new Likes();
        likes.setMember(member);
        likes.setDrawing(drawing);
        likesRepository.save(likes);

        // 좋아요 수 증가
        drawingRepository.increaseLikesCount(drawingId);
    }

    // 좋아요 취소
    @Transactional
    @CacheEvict(value = "likes", key = "#member.memberId")
    public void removeLike(Member member, Long drawingId) {
        // 좋아요 삭제
        likesRepository.deleteByMemberMemberIdAndDrawingDrawingId(member.getMemberId(), drawingId);

        // 좋아요 수 감소
        drawingRepository.decreaseLikesCount(drawingId);
    }

    // 좋아요 확인
    public boolean isAlreadyLiked(Member member, Long drawingId) {
        return likesRepository.existsByMemberMemberIdAndDrawingDrawingId(member.getMemberId(), drawingId);
    }

}
