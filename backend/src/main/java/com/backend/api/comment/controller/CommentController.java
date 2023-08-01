package com.backend.api.comment.controller;


import com.backend.api.comment.service.CommentService;
import com.backend.domain.comment.dto.CommentDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.firebase.service.FCMService;
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
    private final FCMService fcmService;

    @PostMapping("/to/{drawingId}")
    @Operation(summary = "댓글 작성", description = "그림 게시물에 댓글을 생성합니다.")
    public CommentDto createComment(@PathVariable Long drawingId,
                                                 @RequestParam String content,
                                                 @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        // 게시글 작성자의 Firebase 토큰 가져오기
        String authorToken = memberService.getAuthorFirebaseToken(drawingId);
        // 현재 유저의 닉네임
        String CurrentUserNickname = member.getNickname();

        // 작성자에게 알림 보내기
        String title = "뭉게뭉게 도화지";
        String body = String.format("%s 님이 회원님의 그림에 댓글을 남겼습니다.",CurrentUserNickname);
        fcmService.sendFCMNotification(authorToken,title,body);


        return commentService.createComment(drawingId, content, member);

    }

    @DeleteMapping("/delete/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다. 본인이 작성한 댓글만 삭제할 수 있습니다.")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        commentService.deleteComment(commentId, member.getMemberId());
        return ResponseEntity.ok(String.format("%d번 댓글 삭제", commentId));
    }
}
