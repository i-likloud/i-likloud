package com.backend.api.history.dto;

import com.backend.domain.history.constant.HistoryType;
import com.backend.domain.history.entity.History;
import lombok.Getter;

@Getter
public class HistoryDto {

    private final Long historyId;
    private final Long memberId;
    private final String content;
    private final HistoryType historyType;

    public HistoryDto(History history){
        this.historyId = history.getHistoryId();
        this.memberId = history.getMember().getMemberId();
        this.content = history.getContent();
        this.historyType = history.getHistoryType();
    }

}
