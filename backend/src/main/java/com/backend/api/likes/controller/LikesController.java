package com.backend.api.likes.controller;

import com.backend.api.history.service.HistoryService;
import com.backend.api.likes.service.LikesService;
import com.backend.domain.history.constant.HistoryType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.error.ErrorResponse;
import com.backend.global.firebase.service.FCMService;
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

    private final FCMService fcmService;

    private final HistoryService historyService;

    @PostMapping
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "#### 성공"), @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오", content =@Content(schema = @Schema(implementation = ErrorResponse.class), examples = {@ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"), @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"), @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"), @ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."), @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."), @ExampleObject( name = "404_Drawing-001", value = "그림을 찾을 수 없습니다. 그림 id값을 확인해주세요."), @ExampleObject( name = "500", value = "서버에러")}))})
    @Operation(summary = "좋아요", description = "그림을 좋아요 또는 좋아요 해제 합니다."+"\n\n### [ 수행절차 ]\n\n"+"- 좋아요 하고 싶은 그림의 id값을 drawingId에 넣어주세요\n\n"+"- Execute 해주세요\n\n"+"- 이미 좋아요 되어 있다면 좋아요가 해제 됩니다.\n\n"+"- 좋아요가 안되어 있다면 좋아요 처리 됩니다.\n\n")
    public ResponseEntity<String> toggleLike(@PathVariable Long drawingId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        if (likesService.isAlreadyLiked(member, drawingId)) {
            likesService.removeLike(member, drawingId);
            return ResponseEntity.ok(String.format("%d번 게시물 좋아요 취소", drawingId));
        } else {
            likesService.addLike(member, drawingId);

            // 그림 작성자의 Firebase 토큰 가져오기
            String authorToken = memberService.getAuthorFirebaseToken(drawingId);
            Member user = memberService.findMemberByDrawingId(drawingId);

            if (member!= user) {
                // 현재 유저의 닉네임
                String CurrentUserNickname = member.getNickname();

                // 작성자에게 알림 보내기
                String title = "뭉게뭉게 도화지";
                String body = String.format("%s 님이 회원님의 그림을 좋아합니다.", CurrentUserNickname);
                fcmService.sendFCMNotification(authorToken, title, body);

                // HistoryDB에 담기
                historyService.createHistory(body, user, HistoryType.LIKE);
            }

            return ResponseEntity.ok(String.format("%d번 게시물 좋아요", drawingId));
        }
    }

}
