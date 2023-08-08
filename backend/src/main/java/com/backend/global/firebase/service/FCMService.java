package com.backend.global.firebase.service;

import com.backend.api.history.service.HistoryService;
import com.backend.domain.drawing.DrawingService;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.history.constant.HistoryType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FCMService {

    private final DrawingService drawingService;
    private final HistoryService historyService;
    private final MemberService memberService;

    public void sendFCMNotification(String token, String title, String body, Long drawingId, HistoryType historyType, Member member) {
        try{
            // 알림 내용 설정
//            Notification notification = Notification.builder()
//                    .setTitle(title)
//                    .setBody(body)
//                    .build();

            Map<String, String> data = new HashMap<>();
            // 그림 아이디
            data.put("drawingId", String.valueOf(drawingId));
            // 타입
            data.put("historyType", historyType.name());
            // 보낸 사람
            data.put("sentId", String.valueOf(member.getMemberId()));

            // 알림 보낼 대상 설정
            Message message = Message.builder()
//                    .setNotification(notification)
                    .putAllData(data)
                    .setToken(token)
                    .build();


            // FCM으로 알림 전송
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    // 댓글 알림
    public void commentAlert(Long drawingId, Member member) {
        // 게시글 작성자의 Firebase 토큰 가져오기
        Drawing drawing = drawingService.findDrawingById(drawingId);
        // 게시글 작성자 == 알림 받을 사람
        Member creator = drawing.getMember();
        String authorToken = creator.getFirebaseToken();

        if (member != creator) {
            // 댓글 작성자의 닉네임
            String CurrentUserNickname = member.getNickname();

            // 게시글 작성자에게 알림 보내기
            String title = "뭉게뭉게 도화지";
            String body = String.format("%s 님이 회원님의 그림에 댓글을 남겼습니다.", CurrentUserNickname);
            this.sendFCMNotification(authorToken, title, body, drawingId,HistoryType.COMMENT, member);

            // HistoryDB에 담기
            historyService.createHistory(body, creator, HistoryType.COMMENT, drawingId, member.getMemberId());
        }
    }

    // 좋아요 알림
    public void likeAlert(Long drawingId, Member member) {
        // 게시글 작성자의 Firebase 토큰 가져오기
        Drawing drawing = drawingService.findDrawingById(drawingId);
        // 게시글 작성자 == 알림 받을 사람
        Member creator = drawing.getMember();
        String authorToken = creator.getFirebaseToken();

        if (member != creator) {
            // 좋아요 한 유저의 닉네임
            String CurrentUserNickname = member.getNickname();

            // 게시글 작성자에게 알림 보내기
            String title = "뭉게뭉게 도화지";
            String body = String.format("%s 님이 회원님의 그림을 좋아합니다.", CurrentUserNickname);
            this.sendFCMNotification(authorToken, title, body, drawingId,HistoryType.LIKE, member);

            // HistoryDB에 담기
            historyService.createHistory(body, creator, HistoryType.LIKE, drawingId, member.getMemberId());
        }
    }

    // 토큰 선물 알림
    public void tokenGiftAlert(Long toMemberId, Member member) {
        // 선물 받는 사람의 Firebase 토큰 가져오기
        Member toMember = memberService.findMemberById(toMemberId);
        String authorToken = toMember.getFirebaseToken();

        // 현재 유저의 닉네임
        String CurrentUserNickname = member.getNickname();

        // 작성자에게 알림 보내기
        String title = "뭉게뭉게 도화지";
        String body = String.format("%s 님이 그림을 선물하였습니다.",CurrentUserNickname);
        this.sendFCMNotification(authorToken,title,body, null,HistoryType.GIFT,null);

        // HistoryDB에 담기
        historyService.createHistory(body, toMember, HistoryType.LIKE, null,null);

    }
}
