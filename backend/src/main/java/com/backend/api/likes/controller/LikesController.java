package com.backend.api.likes.controller;

import com.backend.api.likes.service.LikesService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Drawing", description = "그림 관련 api")
@RestController
@RequestMapping("/api/drawings/{drawingId}/likes")
@RequiredArgsConstructor
public class LikesController {


    private final LikesService likesService;

    private final MemberService memberService;

//    private final FCMService fcmService;

    @PostMapping
    @Operation(summary = "좋아요", description = "토글 형식으로 이미 좋아요했으면 취소, 아니면 좋아요 등록")
    public ResponseEntity<String> toggleLike(@PathVariable Long drawingId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        if (likesService.isAlreadyLiked(member, drawingId)) {
            likesService.removeLike(member, drawingId);
            return ResponseEntity.ok(String.format("%d번 게시물 좋아요 취소", drawingId));
        } else {
            likesService.addLike(member, drawingId);

            // 그림 작성자의 Firebase 토큰 가져오기
            String authorToken = memberService.getAuthorFirebaseToken(drawingId);

            // 현재 유저의 닉네임
            String CurrentUserNickname = member.getNickname();

            // 작성자에게 알림 보내기
            String title = "뭉게뭉게 도화지";
            String body = String.format("%s 님이 회원님의 그림을 좋아합니다.",CurrentUserNickname);
//            fcmService.sendFCMNotification(authorToken,title,body);
            return ResponseEntity.ok(String.format("%d번 게시물 좋아요", drawingId));
        }
    }

}
