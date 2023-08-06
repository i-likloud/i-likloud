package com.backend.api.drawing.service;

//import com.amazonaws.services.kms.model.NotFoundException;

import com.backend.api.drawing.dto.DrawingDetailDto;
import com.backend.api.drawing.dto.DrawingFromPhotoDto;
import com.backend.api.drawing.dto.DrawingListDto;
import com.backend.domain.comment.repository.CommentRepository;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.likes.repository.LikesRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.nft.repository.NftRepository;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrawingViewService {

    private final DrawingRepository drawingRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final NftRepository nftRepository;


    // 전체 게시물 조회
    @Cacheable(value = "allDrawings", key = "#orderBy", unless = "#orderBy != 'createdAt'")
    public List<DrawingListDto> getAllDrawings(String orderBy){

        List<Drawing> drawings = drawingRepository.findAll(Sort.by(Sort.Direction.DESC, orderBy));

        return drawings.stream()
                .map(DrawingListDto::new)
                .collect(Collectors.toList());
    }

    // 상세 게시물 조회
    @Transactional
    public DrawingDetailDto getDrawing(Long drawingId, Long memberId){
        // 그림 가져올 때 댓글과 함께 가져오기
        Drawing drawing = drawingRepository.findByIdWithComments(drawingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DRAWING));

        // 조회수 증가
        drawing.setViewCount(drawing.getViewCount() + 1);

        // 사용자가 좋아요 했는지 확인
        boolean memberLiked = likesRepository.existsByMemberMemberIdAndDrawingDrawingId(memberId, drawingId);

        return new DrawingDetailDto(drawing, memberLiked);
    }

    // 그림 게시물 삭제
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allDrawings", allEntries = true),
            @CacheEvict(value = "drawings", key = "#memberId")
    })
    public void deleteDrawing(Long drawingId, Long memberId) {

        Drawing drawing = drawingRepository.findById(drawingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DRAWING));
        // 본인 그림 아닌경우
        if(!drawing.getMember().getMemberId().equals(memberId)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_MEMBER);
        }

        // 댓글 일괄 삭제
        commentRepository.deleteCommentByDrawingId(drawingId);
        // 좋아요 일괄 삭제
        likesRepository.deleteLikesByDrawingId(drawingId);

        // NFT의 그림 null 처리
        nftRepository.unlinkNftFromDrawing(drawingId);

        // 그림 삭제
        drawingRepository.delete(drawing);

    }

    // 그림 게시물 수정
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allDrawings", allEntries = true),
            @CacheEvict(value = "drawings", key = "#member.memberId")
    })
    public DrawingFromPhotoDto editDrawing(Long drawingId, Member member, String title, String content){
        Long memberId = member.getMemberId();
        Drawing drawing = drawingRepository.findById(drawingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DRAWING));

        if (!drawing.getMember().getMemberId().equals(memberId)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
        drawing.setTitle(title);
        drawing.setContent(content);

        DrawingFromPhotoDto drawingFromPhotoDto = new DrawingFromPhotoDto(drawing);
        return drawingFromPhotoDto;

    }
}
