package com.backend.api.login.controller;

import com.backend.api.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OauthLoginControllerTestEx extends BaseIntegrationTest {

    @Test
    @Rollback
    void oauthLogin() throws Exception{

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/oauth/login")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + "잘못된 access token 값")
                            .content("{\n" +
                                    "  \"email\": \"testemail@test.com\",\n" +
                                    "  \"socialType\": \"KAKAO\",\n" +
                                    "  \"firebaseToken\": \"string\"\n" +
                                    "}")

                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions.andExpect(status().is5xxServerError());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}