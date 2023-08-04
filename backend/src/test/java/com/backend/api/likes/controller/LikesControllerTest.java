package com.backend.api.likes.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.backend.api.likes.service.LikesService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LikesControllerTest extends BaseIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Mock
    private LikesService likesService;

    private AccessToken accessToken;
    private String token = AccessToken.getNewToken();
    private String userEmail = AccessToken.getTestEmail();


    @Test
    @Rollback
    void toggleLike() {
        //given
        Long drawingId = 1L;
//        String userEmail = "hoilday5303@naver.com";
        Member member = memberService.findMemberByEmail(userEmail);
        boolean isAlreadyLiked = likesService.isAlreadyLiked(member, drawingId);

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/drawings/{drawingId}/likes", drawingId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content("{\"email\": \"" + userEmail + "\"}")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions.andExpect(status().isOk());

            //토글확인
            boolean isNowLiked = likesService.isAlreadyLiked(member, drawingId);
            if(isAlreadyLiked){
            if (!isNowLiked) {
                    System.out.println("토글 실패: 좋아요 취소가 되지 않았습니다.");
                } else {
                    System.out.println("토글 성공: 좋아요 취소가 정상적으로 되었습니다.");
                }
            } else {
                if (isNowLiked) {
                    System.out.println("토글 실패: 좋아요가 정상적으로 되지 않았습니다.");
                } else {
                    System.out.println("토글 성공: 좋아요가 취소되었습니다.");
                }
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }
    }
}