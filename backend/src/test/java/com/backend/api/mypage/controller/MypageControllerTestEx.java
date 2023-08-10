package com.backend.api.mypage.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MypageControllerTestEx extends BaseIntegrationTest {
    @Autowired
    MemberService memberService;
    private String token = AccessToken.getNewToken();
    private String userEmail = AccessToken.getTestEmail();


    @Test
    @Rollback
    void editNickname() throws Exception {

        // given
        Member member = memberService.findMemberByEmail(userEmail);
        String nickname = member.getNickname();

        try {
            //when
            ResultActions resultActions = mvc.perform(put("/api/mypage/nickname" )
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content("{\"email\": \"" + userEmail + "\"}")
                            .param("nickname", nickname)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions.andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
