package com.backend.api.report.controller;

import com.backend.api.member.dto.MemberDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.report.dto.ReportDto;
import com.backend.domain.report.service.ReportService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> createReport(

            @PathVariable Long drawingId,
            @RequestBody ReportDto reportDto,
            @MemberInfo MemberInfoDto memberInfoDto
            ) {

        if (drawingId == null) {
            // drawingId가 null인 경우에 대한 예외 처리
            return ResponseEntity.badRequest().body("drawingId를 지정해주세요.");
        }
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();

        reportService.createReport(memberId, drawingId, reportDto.getContent());
        return ResponseEntity.ok("신고가 정상적으로 처리되었습니다");
    }
}
