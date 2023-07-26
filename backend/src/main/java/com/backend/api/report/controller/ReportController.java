package com.backend.api.report.controller;

import com.backend.api.member.dto.MemberDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.report.dto.ReportDto;
import com.backend.domain.report.service.ReportService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;
    private final MemberService memberService;

    // 신고 생성
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
