package com.backend.api.mypage.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.backend.api.mypage.dto.MypageInfoDto;
import com.backend.api.mypage.service.MypageService;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.member.service.MemberService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class MypageControllerTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mvc;

    private AccessToken accessToken;

    private String token = AccessToken.getToken();
    private String userEmail = AccessToken.getTestEmail();

    @Test
    @Rollback
    void getMyInfo() {
        //given
        Long memberId = 1L;
        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/mypage/home")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                    .andDo(MockMvcResultHandlers.print());



            //then
            resultActions
                    .andExpect(status().isOk());
//                    .andExpect(jsonPath("$").value(1));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void editNickname() throws Exception {

        // given
        String nickname = "새로운닉네임";

        try {
            //when
            ResultActions resultActions = mvc.perform(put("/api/mypage/nickname" )
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content("{\"email\": \"" + userEmail + "\"}")
                            .param("nickname", nickname)
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
    void editProfile() {
        // given
        int profileFace = 9;
        int profileColor = 9;
        int profileAccessory = 9;

        String requestBody = "{"
                + "\"profileColor\": " + profileColor + ","
                + "\"profileFace\": " + profileFace + ","
                + "\"profileAccessory\": " + profileAccessory
                + "}";

        try {
            //when
            ResultActions resultActions = mvc.perform(put("/api/mypage/profile" )
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(requestBody)
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
    void getMyLikes() {
        //given
        Long memberId = 1L;

        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/mypage/likes")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                    .andDo(MockMvcResultHandlers.print());

            // 응답 상태를 확인합니다.
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void getMyDrawings() {
        //given
        Long memberId = 1L;
        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/mypage/drawings")
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
    void getMyBookmarks() {
        //given
        Long memberId = 1L;
        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/mypage/bookmarks")
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
    void getMyPhotos() {
        //given
        Long memberId = 1L;
        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/mypage/photos")
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
    void getMyAccessorys() {
        //given
        Long memberId = 1L;
        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/mypage/accessory")
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