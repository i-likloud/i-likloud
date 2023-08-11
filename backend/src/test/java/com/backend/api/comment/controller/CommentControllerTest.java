package com.backend.api.comment.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends BaseIntegrationTest {

    // 테스트 데이터
    private String token = AccessToken.getNewToken();
    private Long photoId;
    private Long drawingId;

    @BeforeEach
    public void setUp() throws Exception{
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
        drawingId = drawingIdInteger.longValue();

    }

    @Test
    @Rollback
    void createComment() throws Exception {
        // given
        String content = "댓글 내용";

        //when
        ResultActions resultActions = mvc.perform(post("/api/comment/to/{drawingId}", drawingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .param("content", content)

                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        //then
        resultActions.andExpect(status().isOk());

    }

    @Test
    @Rollback
    void createAndDeleteComment() throws Exception{
        // given
        String content = "댓글 내용";

        // when
        // 댓글 생성
        ResultActions createResult = mvc.perform(post("/api/comment/to/{drawingId}", drawingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .param("content", content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        // 생성된 댓글의 ID 받아오기
        String createResponse = createResult.andReturn().getResponse().getContentAsString();
        Integer commentIdInteger = JsonPath.read(createResponse, "$.commentId");
        Long commentId = commentIdInteger.longValue();

        // 댓글 삭제
        ResultActions deleteResult = mvc.perform(delete("/api/comment/delete/{commentId}", commentId)
                        .header("Authorization", "Bearer " + token))
                .andDo(MockMvcResultHandlers.print());

        // then
        deleteResult.andExpect(status().isOk());

    }

}