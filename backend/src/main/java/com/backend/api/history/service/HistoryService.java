package com.backend.api.history.service;

import com.backend.api.history.dto.HistoryDto;
import com.backend.domain.history.constant.HistoryType;
import com.backend.domain.history.entity.History;
import com.backend.domain.history.repository.HistoryRepository;
import com.backend.domain.member.entity.Member;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HistoryService {
    private final HistoryRepository historyRepository;
    
    // 내 히스토리 보기
    public List<HistoryDto> getMyHistory(Long memberId) {
        List<History> list = historyRepository.findAllByMemberMemberIdOrderByCreatedAtDesc(memberId);

        return list.stream()
                .map(HistoryDto::new)
                .collect(Collectors.toList());
    }
    
    // 히스토리 생성(nft, 좋아요, 북마크)
    public void createHistory(String content, Member member, HistoryType historyType, Long drawingId, Long sendId){
        History history = new History();
        history.setContent(content);
        history.setMember(member);
        history.setHistoryType(historyType);
        history.setDrawingId(drawingId);
        history.setSendId(sendId);
        historyRepository.save(history);
    }
    
    // 히스토리 삭제
    @Transactional
    public void deleteHistory(Long historyId, Long memberId){
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_COMMENT));

        if(!history.getMember().getMemberId().equals(memberId)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
        historyRepository.delete(history);
    }


}
