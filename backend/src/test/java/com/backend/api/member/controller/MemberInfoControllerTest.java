package com.backend.api.member.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberInfoControllerTest extends BaseIntegrationTest {


    private String token = AccessToken.getNewToken();

    private String userEmail = AccessToken.getTestEmail();
    @Test
    @Rollback
    void getMemberInfo() {
        //given

        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/member/info")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                    .andDo(MockMvcResultHandlers.print());



            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value(userEmail));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    @Rollback
    void updateAdditionalInfo() {
        // given
        String nickname = "새로운닉네임";
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
            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void memberSearchInfo() {
        //given
        Long memberId = 1L;
        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/member/search/{memberId}",memberId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                    .andDo(MockMvcResultHandlers.print());



            //then
            resultActions
                    .andExpect(status().isOk());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}