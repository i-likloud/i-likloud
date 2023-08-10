package com.backend.api.report.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ReportControllerTestEX extends BaseIntegrationTest {

    private String token = AccessToken.getNewToken();
    private String userEmail = AccessToken.getTestEmail();

    @Test
    @Rollback
    void createReport() throws Exception {

        // given
        Long drawingId = 999L;
        String content = "내용";

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/report/{drawingId}", drawingId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content("{\"email\": \"" + userEmail + "\"}")
                            .param("content", content)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions.andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}