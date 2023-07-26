package com.backend.api.likes.controller;

import com.backend.api.likes.service.LikesService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drawings/{drawingId}/likes")
@RequiredArgsConstructor
public class LikesController {


    private final LikesService likesService;

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "그림 게시물 좋아요")
    public ResponseEntity<String> addLike(@PathVariable Long drawingId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        likesService.addLike(member, drawingId);
        return ResponseEntity.ok(String.format("%d번 게시물 좋아요", drawingId));
    }

    @DeleteMapping
    @Operation(summary = "그림 게시물 좋아요 취소")
    public ResponseEntity<String> removeLike(@PathVariable Long drawingId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        likesService.removeLike(member, drawingId);
        return ResponseEntity.ok(String.format("%d번 게시물 좋아요 취소", drawingId));
    }
}
