package com.backend.api.history.service;

import com.backend.api.history.dto.HistoryDto;
import com.backend.domain.history.constant.HistoryType;
import com.backend.domain.history.entity.History;
import com.backend.domain.history.repository.HistoryRepository;
import com.backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


}
