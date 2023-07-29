package com.backend.api.report.controller;

import com.backend.api.report.dto.ReportResponseDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.report.entity.Report;
import com.backend.domain.report.service.ReportService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Report", description = "신고 관련 api")
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;
    private final MemberService memberService;

    // 신고 생성
    @Operation(summary = "신고하기", description = "특정 게시글을 신고할 수 있는 메소드입니다.")
    @PostMapping("/{drawingId}")
    public ReportResponseDto createReport(
            @PathVariable Long drawingId,
            @RequestParam String content,
            @MemberInfo MemberInfoDto memberInfoDto
            ) {

        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Report report = reportService.createReport(findMember, drawingId, content);

        return new ReportResponseDto(report, drawingId);
    }
}
