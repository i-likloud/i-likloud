package com.backend.domain.report.service;

import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.report.entity.Report;
import com.backend.domain.report.repository.ReportRepository;
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
    public void createReport(Long memberId, Long drawingId, String content){
        System.out.println(memberId);
        System.out.println(drawingId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Reporter not found with ID: " + memberId));

        Drawing drawing = drawingRepository.findById(drawingId)
                .orElseThrow(() -> new IllegalArgumentException("Drawing not found with ID: " + drawingId));

        Report report = Report.builder()
                .memberId(member)
                .drawingId(drawing)
                .content(content)
                .build();
        reportRepository.save(report);
    }
}
