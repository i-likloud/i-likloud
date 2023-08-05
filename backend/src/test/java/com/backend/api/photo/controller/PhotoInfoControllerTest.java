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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers; // 이 부분을 추가해주세요

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class PhotoInfoControllerTest extends BaseIntegrationTest {


    @Autowired
    private MemberService memberService;

    @Mock
    private PhotoInfoService photoInfoService;
    
    private AccessToken accessToken;

//    String token = accessToken.token;
    private String token = AccessToken.getNewToken();
//    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2OTA5NjYyNDksImV4cCI6MTY5MDk2ODA0OSwiZW1haWwiOiJob2lsZGF5NTMwM0BuYXZlci5jb20iLCJyb2xlIjoiTUVNQkVSIn0.-Bz9EN1uubisajaCATX7ds7gR5_h5a5ht5ljVMk2QLkrOV_P4fmuFSLBU6bxVEofG7VxRSGgltxdXi7Fy2SNuQ";

    private String userEmail = AccessToken.getTestEmail();

    @Test
    @Rollback
    void searchAllDesc() throws Exception {
        //given
//        System.out.println("테스트 " + tokens);
        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/photo/new")
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

    @Test
    @Rollback
    void searchAllPickCntDesc() throws Exception {
        //given
        //when
        try {
        ResultActions resultActions = mvc.perform(get("/api/photo/pick")
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

    @Test
    @Rollback
    void searchAllBookmarkCntDesc() throws Exception {
        //given
        //when
            try {
        ResultActions resultActions = mvc.perform(get("/api/photo/bookmarkdesc")
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

    @Test
    @Rollback
    void getDrawingsByPhotoId() throws Exception {
        //given
        Long photoId = 1L;
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


    @Test
    @Rollback
    void photoDelete() throws Exception {
        //given
        Long photoId = 1L;
//        System.out.println("createPhotoAndGetId " + photoId);
        //when
        try{
        ResultActions resultActions = mvc.perform(delete("/api/photo/delete/{photoId}", photoId)
                        .header("Authorization", "Bearer " + token))
                .andDo(MockMvcResultHandlers.print());

        resultActions.andExpect(status().isOk());
        }catch (Exception e){
            e.printStackTrace();
        }
        //then

    }

    @Test
    @Rollback
    void pickPhoto() throws Exception {
        //given
        Long photoId = 1L;
//        String userEmail = "hoilday5303@naver.com";
        Member member = memberService.findMemberByEmail(userEmail);
        boolean isAlreadyBookmarked = photoInfoService.isAlreadyBookmarked(member, photoId);

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/photo/{photoId}/bookmark", photoId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content("{\"email\": \"" + userEmail + "\"}")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions.andExpect(status().isOk());

            //토글확인
            boolean isNowBookmarked = photoInfoService.isAlreadyBookmarked(member, photoId);
            if (isAlreadyBookmarked) {
                if (!isNowBookmarked) {
                    System.out.println("토글 실패: 픽 취소가 되지 않았습니다.");
                } else {
                    System.out.println("토글 성공: 픽 취소가 정상적으로 되었습니다.");
                }
            } else {
                if (isNowBookmarked) {
                    System.out.println("토글 실패: 픽이 정상적으로 되지 않았습니다.");
                } else {
                    System.out.println("토글 성공: 픽이 취소되었습니다.");
                }
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }
    }


}
