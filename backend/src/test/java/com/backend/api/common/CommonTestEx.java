package com.backend.api.common;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommonTestEx extends BaseIntegrationTest {

    private String expiredToken = "";
    private String refreshToken = "";
    private String guestToken = "";

    @Test
    @Rollback
    // 401_Auth-006: 인증 토큰을 아예 안넣은 경우
    void ExampleNoToken() throws Exception{
        try {
            ResultActions resultActions = mvc.perform(post("/api/oauth/login")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer ")
//                            .content("{" +
//                                    "}")
//                            .param("example")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    // 401_Auth-005: access 토큰을 잘못 넣은 경우 (refresh토큰, 기간만료 토큰도 아닌 잘못된 값)
    void ExampleWrongToken() throws Exception{
        try {
            ResultActions resultActions = mvc.perform(post("/api/oauth/login")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer "+ "잘못된 access token 값")
//                            .content("{" +
//                                    "}")
//                            .param("example")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    // 401_Auth-001: 만료된 access 토큰을 넣은 경우
    void ExampleExpiredToken() throws Exception{
        try {
            ResultActions resultActions = mvc.perform(post("/api/oauth/login")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer "+ expiredToken)
//                            .content("{" +
//                                    "}")
//                            .param("example")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    // 401_Auth-004: refresh 토큰을 넣은 경우 (토큰재발급 제외)
    void ExampleRefreshToken() throws Exception{
        try {
            ResultActions resultActions = mvc.perform(post("/api/oauth/login")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer "+ refreshToken)
//                            .content("{" +
//                                    "}")
//                            .param("example")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    // 401_Auth-009: 추가정보를 안넣어 guest 토큰을 넣은 경우
    void ExampleGuestToken() throws Exception{
        try {
            ResultActions resultActions = mvc.perform(post("/api/oauth/login")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer "+ guestToken)
//                            .content("{" +
//                                    "}")
//                            .param("example")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}