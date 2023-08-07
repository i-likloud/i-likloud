package com.backend.api.nft.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NftTokenControllerTest extends BaseIntegrationTest {
//    private AccessToken accessToken;
//    private String token = AccessToken.getNewToken();

    @Test
    @Rollback
    void createWallet() {
        //지갑을 생성하지 않은 member의 token값
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJImV4cCI6M3ZXp6ZXJ5QGRhdW0ubmV0Iiwicm9sZSI6Ik1FTUJFUiJ9.dzp8kw4Co75_vWqGBP32H6wLX-uVlmIPruX3TVtxCwm9qNOpxjbf6JfTrxMkGGXEQXOUBFErOum5yM0O_PXJSg";

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/nft/wallet")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
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
        //지갑을 생성한 member의 token값
        String token ="eyJ0eXAiOiJKV1OTc0NDksImV4cCI6MTY5MTM5OTIMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.X5ax_CiASUbYBPmFHfzV3Zxvw1TR15ILLkcsaSPFacSWNKCQg6T9Lzyholsa1qyDB-6YY_0pD13UcNjhBIyyzQ";
        Long drawingId = 3L;

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/nft/token/{drawingId}",drawingId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
            //then
            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void getTokenList() {
        String token ="eyJ0eXAiOi2OTEzOTc5NDksImV4cCxlIjoiTUVNQkVSIn0.PRlcDgGtKvb_dG3fuJvE0AbXzuFtY8goGCB_Ou2XAweDojzg6i80Ycnh8D0-aj8XMDcGjAPibhXAXSVoc24Q2g";
        Long memberId = 1L;
        try {
            //when
            ResultActions resultActions = mvc.perform(get("/api/nft/wallet/{memberId}",memberId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
            //then
            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void getAllTokenList() {
        String token ="eyJ0eXAiOiJKV1QiLCJhbGciMTY5MTM5OTc0OSwiZW1haWwiOiJod2Nob2kzM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.PRlcDgGtKvb_dG3fuJvE0AbXzuFtY8goGCB_Ou2XAweDojzg6i80Ycnh8D0-aj8XMDcGjAPibhXAXSVoc24Q2g";
        try {
            //when
            ResultActions resultActions = mvc.perform(get("/api/nft/token")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
            //then
            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void transferToken() {
        //nft발급한 member의 token값
        String token ="eyJ0eXAiTQxMzg5MSwiZW1haWwiOiJod2NoIjoiTUVNQkVSIn0.6Gf686XsyINSvidPhj2AQ2W276soL1M2ueDfaM_gtUFZHXjd8Cs3M4h2FKksFdafhvANbUYLnBW2B1tT5BRs_g";

        //위 토큰이 발급한 nftId
        Long nftId = 2L;

        //member중에 지갑있는 memeberId
        Long toMemberId = 5L;

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/nft/token/{nftId}/to/{toMemberId}",nftId, toMemberId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .param("message", "testmessage")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}