package com.backend.domain.report.service;

import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.report.entity.Report;
import com.backend.domain.report.repository.ReportRepository;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final DrawingRepository drawingRepository;

    @Transactional
    public Report createReport(Member member, Long drawingId, String content){
        System.out.println(member.getMemberId());
        System.out.println(drawingId);

        Drawing drawing = drawingRepository.findById(drawingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DRAWING));

        Report report = Report.builder()
                .member(member)
                .drawing(drawing)
                .content(content)
                .build();
        reportRepository.save(report);

        return report;
    }
}
