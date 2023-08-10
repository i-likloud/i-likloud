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


    private String token = AccessToken.getNewToken();

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
                    .andExpect(status().isUnauthorized());
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
                .andExpect(status().is5xxServerError());
//                .andExpect(status().isNotFound());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
