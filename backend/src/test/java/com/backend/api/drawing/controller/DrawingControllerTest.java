package com.backend.api.drawing.controller;


import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.backend.api.drawing.dto.DrawingUploadDto;
import com.backend.api.drawing.service.DrawingUploadService;
import com.backend.api.drawing.service.DrawingViewService;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.HashMap;
import java.util.Map;

import static com.backend.domain.member.constant.SocialType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DrawingControllerTest extends BaseIntegrationTest {

    @Autowired
    private MemberService memberService;

//    @Mock
//    private DrawingViewService drawingViewService;
//
//    @Mock
//    private DrawingUploadService drawingUploadService;

    private AccessToken accessToken;

    private String token = AccessToken.getNewToken();
    private String userEmail = AccessToken.getTestEmail();

//    @Test
//    @Rollback
//    void uploadDrawing() throws Exception {
//        //given
//        Long photoId = 1L;
//        Member member = memberService.findMemberByEmail(userEmail);
//
//        ClassPathResource resource = new ClassPathResource("1915_20200114094530503.jpg");
//        MockMultipartFile file = new MockMultipartFile("file", resource.getFilename(), MediaType.IMAGE_JPEG_VALUE, resource.getInputStream());
//
//        System.out.println("존재: " + resource.exists());
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("title", "제목");
//        requestBody.put("content", "내용");
//
//
//
//        MockHttpServletRequestBuilder requestBuilder = multipart("/api/drawings/upload/from/{photoId}", photoId)
//                .file(file)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(requestBody))
//                .header("Authorization", "Bearer " + token);
//
//
//        //when
//
//        ResultActions resultActions = mvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//        //then
//
//
//
//    }

    @Test
    @Rollback
    void drawingBoard() {
        //given

        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/drawings/?orderBy=likesCount")
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
    void getDrawing() {
        //given
        Long drawingId = 1L;
        //when
        try{
            ResultActions resultActions = mvc.perform(get("/api/drawings/{drawingId}", drawingId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.drawingId").value(1));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    void deleteDrawing() {
        //given
        Long drawingId = 1L;
        //when
        try{
            ResultActions resultActions = mvc.perform(delete("/api/photo/delete/{drawingId}", drawingId)
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
    void deleteDrawingEx() {
        //given
        Long drawingId = 1L;
        //when
        try{
            ResultActions resultActions = mvc.perform(delete("/api/photo/delete/{drawingId}", drawingId)
                            .header("Authorization", "Bearer " + token))
                    .andDo(MockMvcResultHandlers.print());

            resultActions.andExpect(status().isOk());
        }catch (Exception e){
            e.printStackTrace();
        }
        //then
    }
}