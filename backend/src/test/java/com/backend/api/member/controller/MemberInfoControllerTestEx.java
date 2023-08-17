package com.backend.api.member.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberInfoControllerTestEx extends BaseIntegrationTest {

    @Autowired
    private MemberService memberService;
    private String token = AccessToken.getNewToken();

    private String userEmail = AccessToken.getTestEmail();
    @Test
    @Rollback
    void getMemberInfo() {
        //given
        String token = "임의의 틀린 토큰";
        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/member/info")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                    .andDo(MockMvcResultHandlers.print());
            //then
            resultActions
                    .andExpect(status().isUnauthorized());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void updateAdditionalInfo() {
        Member member = memberService.findMemberByEmail(userEmail);
        // given
        String nickname = member.getNickname();
        int profileFace = 9;
        int profileColor = 9;
        int profileAccessory = 9;

        String requestBody = "{"
                + "\"nickname\": \"" + nickname + "\","
                + "\"profileColor\": " + profileColor + ","
                + "\"profileFace\": " + profileFace + ","
                + "\"profileAccessory\": " + profileAccessory
                + "}";

        try {
            // when
            ResultActions resultActions = mvc.perform(patch("/api/member/additional" )
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + AccessToken.getSecondToken())
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            // then
            resultActions.andExpect(status().isUnauthorized());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void memberSearchInfo() {
        //given
        Long memberId = 9999L;
        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/member/search/{memberId}",memberId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions
                    .andExpect(status().is5xxServerError());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
