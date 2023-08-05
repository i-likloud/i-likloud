package com.backend.api.nft.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NftTokenControllerTest extends BaseIntegrationTest {

    private String token = AccessToken.getToken();

    @Test
    @Rollback
    void createWallet() {
        String token = AccessToken.getToken();
        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/nft/wallet")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                    .andDo(MockMvcResultHandlers.print());
            //then
            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void createToken() {
        Long drawingId = 1L;




    }

    @Test
    @Rollback
    void getTokenList() {
    }

    @Test
    @Rollback
    void getAllTokenList() {
    }
}