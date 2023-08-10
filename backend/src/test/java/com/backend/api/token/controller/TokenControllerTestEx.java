package com.backend.api.token.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TokenControllerTestEx extends BaseIntegrationTest {


    private String refreshToken= "임의의 refresh 토큰";

    @Test
    @Rollback
    void createAccessToken() throws Exception{
        // given
        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/accounts/access-token/re")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + refreshToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions.andExpect(status().isUnauthorized());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}