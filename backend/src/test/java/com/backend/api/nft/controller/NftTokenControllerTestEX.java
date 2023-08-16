package com.backend.api.nft.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NftTokenControllerTestEX extends BaseIntegrationTest {

    private String token = AccessToken.getNewToken();

    @Test
    @Rollback
    void createWalletAlreadyGotWallet() {
        //이미 지갑을 생성한 member의 token값

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/nft/wallet")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
            //then
            resultActions.andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void createTokenNoWallet() {
        //지갑이 없는 member의 token값
        String nowallettoken ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2OTE2MjczNjEsImV4cCI6MTY5MTYyOTE2MSwiZW1haWwiOiJob2lsZGF5NTMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.aiHEYa4FlrcgLAO6rCKtcNq7W39dXUd55dNdwRVtpvsDWO62_GrXLXlIkhvSxJJ4fjBB23WDkgYE2JYM2OhXlg";
        Long drawingId = 7L;

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/nft/token/{drawingId}",drawingId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + nowallettoken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
            //then
            resultActions.andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void createTokenNotMyDrawing() {
        //자기가 그린 그림이 아닌경우
        Long drawingId = 7L;

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/nft/token/{drawingId}",drawingId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
            //then
            resultActions.andExpect(status().isUnauthorized());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void createTokenNotExistDrawing() {

        Long drawingId = 9999999L;

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/nft/token/{drawingId}",drawingId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
            //then
            resultActions.andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void createTokenAleradyCreated() {

        Long drawingId = 2L;

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/nft/token/{drawingId}",drawingId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
            //then
            resultActions.andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void getTokenList() {
        Long memberId = 77L;
        try {
            //when
            ResultActions resultActions = mvc.perform(get("/api/nft/wallet/{memberId}",memberId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
            //then
            resultActions.andExpect(status().is5xxServerError());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void transferTokenNoWallet() {
        Long nftId = 2L;
        //member중에 지갑이 없는 memeberId
        Long toMemberId = 7L;

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/nft/token/{nftId}/to/{toMemberId}",nftId, toMemberId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .param("message", "testmessage")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            resultActions.andExpect(status().is5xxServerError());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
