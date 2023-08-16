package com.backend.api.report.dto;

import com.backend.domain.report.entity.Report;
import lombok.Getter;

@Getter
public class ReportResponseDto {

    private final Long reportId;
    private final Long drawingId;
    private final Long reportMemberId;
    private final String content;

    public ReportResponseDto(Report report, Long drawingId) {
        this.reportId = report.getReportId();
        this.reportMemberId = report.getMember().getMemberId();
        this.drawingId = drawingId;
        this.content = report.getContent();
    }
}
