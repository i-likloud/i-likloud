package com.backend.api.report.controller;

import com.backend.api.report.dto.ReportResponseDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.report.entity.Report;
import com.backend.domain.report.service.ReportService;
import com.backend.global.error.ErrorResponse;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "신고하기", description = "특정 게시글을 신고할 수 있는 메소드입니다."+"\n\n### [ 수행절차 ]\n\n"+"- 적합하지 않아 신고하려는 그림의 id값을 drawingId에 넣어주세요\n\n"+"- 사유(신고내용)을 content에 넣어주세요\n\n"+"- 요구하는 모든 값을 넣어줘야 합니다.\n\n"+"- Execute 해주세요\n\n")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "#### 성공"), @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오", content =@Content(schema = @Schema(implementation = ErrorResponse.class), examples = { @ExampleObject( name = "400_User-004", value = "해당 회원은 존재하지 않습니다."), @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"), @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"), @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"), @ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."), @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."), @ExampleObject( name = "404_Drawing-001", value = "그림을 찾을 수 없습니다. 그림 id값을 확인해주세요."), @ExampleObject( name = "500", value = "서버에러")}))})
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
