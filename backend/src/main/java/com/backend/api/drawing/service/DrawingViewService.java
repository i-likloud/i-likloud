package com.backend.api.drawing.service;

//import com.amazonaws.services.kms.model.NotFoundException;
import com.backend.api.drawing.dto.DrawingDetailDto;
import com.backend.api.drawing.dto.DrawingListDto;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.likes.repository.LikesRepository;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
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

    // 전체 게시물 조회
    public List<DrawingListDto> getAllDrawings(Long memberId, String orderBy){
        List<Drawing> drawings = drawingRepository.findAll(Sort.by(Sort.Direction.DESC, orderBy));
        return drawings.stream()
                .map(drawing -> {
                    boolean memberLiked = likesRepository.existsByMemberMemberIdAndDrawingDrawingId(memberId, drawing.getDrawingId());
                    return new DrawingListDto(drawing, memberLiked);
                })
                .collect(Collectors.toList());
    }

    // 상세 게시물 조회
    @Transactional
    public DrawingDetailDto getDrawing(Long drawingId, Long memberId){
        Drawing drawing = drawingRepository.findById(drawingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DRAWING));

        // 조회수 증가
        drawing.setViewCount(drawing.getViewCount() + 1);

        // 사용자가 좋아요 했는지 확인
        boolean memberLiked = likesRepository.existsByMemberMemberIdAndDrawingDrawingId(memberId, drawingId);

        return new DrawingDetailDto(drawing, memberLiked);
    }

    // 그림 게시물 삭제
    @Transactional
    public void deleteDrawing(Long drawingId, Long memberId) {
        Drawing drawing = drawingRepository.findById(drawingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DRAWING));

        if(!drawing.getMember().getMemberId().equals(memberId)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_DELETION);
        }
        drawingRepository.delete(drawing);
    }
}
