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

    public List<HistoryDto> getMyHistory(Long memberId) {
        List<History> list = historyRepository.findAllByMemberMemberId(memberId);

        return list.stream()
                .map(HistoryDto::new)
                .collect(Collectors.toList());
    }

    public void createHistory(String content, Member member,HistoryType historyType){
        History history = new History();
        history.setContent(content);
        history.setMember(member);
        history.setHistoryType(historyType);
        historyRepository.save(history);
    }

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
