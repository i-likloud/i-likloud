package com.backend.api.drawing.controller;


import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.backend.api.drawing.dto.DrawingListDto;
import com.backend.api.drawing.service.DrawingViewService;
import com.backend.domain.photo.entity.Photo;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DrawingControllerTest extends BaseIntegrationTest {

    private String token = AccessToken.getNewToken();
    private Long photoId;

    @BeforeEach
    public void setUp() throws Exception {
        // 사진 업로드
        ClassPathResource resource = new ClassPathResource("1915_20200114094530503.jpg");
        MockMultipartFile file = new MockMultipartFile("multipartFiles", resource.getFilename(), MediaType.IMAGE_JPEG_VALUE, resource.getInputStream());

        MockHttpServletRequestBuilder requestBuilder = multipart("/api/photo/upload")
                .file(file)
                .header("Authorization", "Bearer " + token);

        MvcResult photoUploadResult = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String photoResponse = photoUploadResult.getResponse().getContentAsString();
        System.out.println("Photo Response: " + photoResponse);

        Integer photoIdInteger = JsonPath.read(photoResponse, "$[0].photoId");
        // 사진Id
        photoId = photoIdInteger.longValue();
    }


    @Test
    @Rollback
    void uploadDrawing() throws Exception {
        //given
        ClassPathResource resource2 = new ClassPathResource("1915_20200114094530503.jpg");
        MockMultipartFile file2 = new MockMultipartFile("file", resource2.getFilename(), MediaType.IMAGE_JPEG_VALUE, resource2.getInputStream());

        String title = "제목";
        String content = "내용";

        MockPart titlePart = new MockPart("title", title.getBytes());
        MockPart contentPart = new MockPart("content", content.getBytes());

        //when
        MockHttpServletRequestBuilder requestBuilder2 = multipart("/api/drawings/upload/from/{photoId}", photoId)
                .file(file2)
                .part(titlePart)      // title을 멀티파트로 전송
                .part(contentPart)  // content를 멀티파트로 전송
                .header("Authorization", "Bearer " + token);

        //then
        ResultActions resultActions = mvc.perform(requestBuilder2)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    @Rollback
    void drawingBoard() throws Exception{
        //given
        // 그림 업로드
        ClassPathResource resource2 = new ClassPathResource("1915_20200114094530503.jpg");
        MockMultipartFile file2 = new MockMultipartFile("file", resource2.getFilename(), MediaType.IMAGE_JPEG_VALUE, resource2.getInputStream());

        String title = "제목";
        String content = "내용";

        MockPart titlePart = new MockPart("title", title.getBytes());
        MockPart contentPart = new MockPart("content", content.getBytes());

        MockHttpServletRequestBuilder requestBuilder2 = multipart("/api/drawings/upload/from/{photoId}", photoId)
                .file(file2)
                .part(titlePart)      // title을 멀티파트로 전송
                .part(contentPart)  // content를 멀티파트로 전송
                .header("Authorization", "Bearer " + token);

        MvcResult drawingUploadResult = mvc.perform(requestBuilder2)
                .andExpect(status().isOk())
                .andReturn();

        String drawingResponse = drawingUploadResult.getResponse().getContentAsString();
        System.out.println("Drawing Response: " + drawingResponse);

        //when
        ResultActions resultActions = mvc.perform(get("/api/drawings/")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("orderBy", "createdAt")
                        .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                .andDo(MockMvcResultHandlers.print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
//                .andExpect(jsonPath("$[0].someProperty").value(drawingResponse)); // 특정 속성 값을 확인하는 예시

        // 응답 내용 출력 (선택 사항)
        MvcResult mvcResult = resultActions.andReturn();
        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

    }

    @Test
    @Rollback
    void getDrawing() throws Exception{
        //given
        // 그림 업로드
        ClassPathResource resource2 = new ClassPathResource("1915_20200114094530503.jpg");
        MockMultipartFile file2 = new MockMultipartFile("file", resource2.getFilename(), MediaType.IMAGE_JPEG_VALUE, resource2.getInputStream());

        String title = "제목";
        String content2 = "내용";

        MockPart titlePart = new MockPart("title", title.getBytes());
        MockPart contentPart = new MockPart("content", content2.getBytes());

        MockHttpServletRequestBuilder requestBuilder2 = multipart("/api/drawings/upload/from/{photoId}", photoId)
                .file(file2)
                .part(titlePart)      // title을 멀티파트로 전송
                .part(contentPart)  // content를 멀티파트로 전송
                .header("Authorization", "Bearer " + token);

        MvcResult drawingUploadResult = mvc.perform(requestBuilder2)
                .andExpect(status().isOk())
                .andReturn();

        String drawingResponse = drawingUploadResult.getResponse().getContentAsString();
        System.out.println("Drawing Response: " + drawingResponse);
        Integer drawingIdInteger = JsonPath.read(drawingResponse, "$.drawingId");
        // 그림Id
        Long drawingId = drawingIdInteger.longValue();
        //when
        ResultActions resultActions = mvc.perform(get("/api/drawings/{drawingId}", drawingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                .andDo(MockMvcResultHandlers.print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.drawingId").value(drawingId));
    }

    @Test
    @Rollback
    void deleteDrawing() throws Exception{
        //given
        // 그림 업로드
        ClassPathResource resource2 = new ClassPathResource("1915_20200114094530503.jpg");
        MockMultipartFile file2 = new MockMultipartFile("file", resource2.getFilename(), MediaType.IMAGE_JPEG_VALUE, resource2.getInputStream());

        String title = "제목";
        String content2 = "내용";

        MockPart titlePart = new MockPart("title", title.getBytes());
        MockPart contentPart = new MockPart("content", content2.getBytes());

        MockHttpServletRequestBuilder requestBuilder2 = multipart("/api/drawings/upload/from/{photoId}", photoId)
                .file(file2)
                .part(titlePart)      // title을 멀티파트로 전송
                .part(contentPart)  // content를 멀티파트로 전송
                .header("Authorization", "Bearer " + token);

        MvcResult drawingUploadResult = mvc.perform(requestBuilder2)
                .andExpect(status().isOk())
                .andReturn();

        String drawingResponse = drawingUploadResult.getResponse().getContentAsString();
        System.out.println("Drawing Response: " + drawingResponse);
        Integer drawingIdInteger = JsonPath.read(drawingResponse, "$.drawingId");
        // 그림Id
        Long drawingId = drawingIdInteger.longValue();

        //when
        try{
            ResultActions resultActions = mvc.perform(delete("/api/drawings/delete/{drawingId}", drawingId)
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
    void deleteDrawingEx() throws Exception{
        //given
        // 그림 업로드
        ClassPathResource resource2 = new ClassPathResource("1915_20200114094530503.jpg");
        MockMultipartFile file2 = new MockMultipartFile("file", resource2.getFilename(), MediaType.IMAGE_JPEG_VALUE, resource2.getInputStream());

        String title = "제목";
        String content2 = "내용";

        MockPart titlePart = new MockPart("title", title.getBytes());
        MockPart contentPart = new MockPart("content", content2.getBytes());

        MockHttpServletRequestBuilder requestBuilder2 = multipart("/api/drawings/upload/from/{photoId}", photoId)
                .file(file2)
                .part(titlePart)      // title을 멀티파트로 전송
                .part(contentPart)  // content를 멀티파트로 전송
                .header("Authorization", "Bearer " + token);

        MvcResult drawingUploadResult = mvc.perform(requestBuilder2)
                .andExpect(status().isOk())
                .andReturn();

        String drawingResponse = drawingUploadResult.getResponse().getContentAsString();
        System.out.println("Drawing Response: " + drawingResponse);
        Integer drawingIdInteger = JsonPath.read(drawingResponse, "$.drawingId");
        // 그림Id
        Long drawingId = drawingIdInteger.longValue();

        //when
        try{
            ResultActions resultActions = mvc.perform(delete("/api/drawings/delete/{drawingId}", drawingId)
                            .header("Authorization", "Bearer " + token))
                    .andDo(MockMvcResultHandlers.print());

            resultActions.andExpect(status().isOk());
        }catch (Exception e){
            e.printStackTrace();
        }
        //then
    }
}