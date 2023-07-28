package com.backend.api.comment.controller;


import com.backend.api.comment.dto.CommentRequsetDto;
import com.backend.api.comment.service.CommentService;
import com.backend.domain.comment.dto.CommentDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "댓글 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @PostMapping("/to/{drawingId}")
    @Operation(summary = "댓글 작성", description = "그림 게시물에 댓글을 생성합니다. URL에 작성할 그림 게시물의 ID를 넣어주세요.")
    public CommentDto createComment(@PathVariable Long drawingId,
                                                 @RequestBody CommentRequsetDto request,
                                                 @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        return commentService.createComment(drawingId, request.getContent(), member);
    }

    @DeleteMapping("/delete/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다. 본인이 작성한 댓글만 삭제할 수 있습니다.")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        commentService.deleteComment(commentId, member.getMemberId());
        return ResponseEntity.ok(String.format("%d번 댓글 삭제", commentId));
    }
}
