package com.backend.api.photo.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.backend.api.photo.service.PhotoInfoService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest

class PhotoInfoControllerTestEx extends BaseIntegrationTest {


    @Autowired
    private MemberService memberService;

    @Mock
    private PhotoInfoService photoInfoService;
    
    private AccessToken accessToken;

//    String token = accessToken.token;
    private String token = AccessToken.getToken();
//    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2OTA5NjYyNDksImV4cCI6MTY5MDk2ODA0OSwiZW1haWwiOiJob2lsZGF5NTMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.-Bz9EN1uubisajaCATX7ds7gR5_h5a5ht5ljVMk2QLkrOV_P4fmuFSLBU6bxVEofG7VxRSGgltxdXi7Fy2SNuQ";

    
    // 권한 값 지정 X
    @Test
    @Rollback
    void searchAllDesc() throws Exception {
        //given
        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/photo/new")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + "임의의 권한값")) // "Authorization" 헤더에 토큰 추가
                    .andDo(MockMvcResultHandlers.print());


            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //photoId가 없는 Id일 때
    @Test
    @Rollback
    void getDrawingsByPhotoId() throws Exception {
        //given
        Long photoId = 9999L;
        //when
        try{
        ResultActions resultActions = mvc.perform(get("/api/photo/{photoId}/alldrawings", photoId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                .andDo(MockMvcResultHandlers.print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        }catch (Exception e){
            e.printStackTrace();
        }
    }




}
