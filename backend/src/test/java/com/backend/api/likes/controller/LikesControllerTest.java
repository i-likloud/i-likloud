package com.backend.api.likes.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.backend.api.likes.service.LikesService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LikesControllerTest extends BaseIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Mock
    private LikesService likesService;

    private Long photoId;
    private Long drawingId;
    private String token = AccessToken.getNewToken();
    private String userEmail = AccessToken.getTestEmail();

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
    void toggleLike() {
        //given

//        String userEmail = "hoilday5303@naver.com";
        Member member = memberService.findMemberByEmail(userEmail);
        boolean isAlreadyLiked = likesService.isAlreadyLiked(member, drawingId);

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/drawings/{drawingId}/likes", drawingId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content("{\"email\": \"" + userEmail + "\"}")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions.andExpect(status().isOk());

            //토글확인
            boolean isNowLiked = likesService.isAlreadyLiked(member, drawingId);
            if(isAlreadyLiked){
            if (!isNowLiked) {
                    System.out.println("토글 실패: 좋아요 취소가 되지 않았습니다.");
                } else {
                    System.out.println("토글 성공: 좋아요 취소가 정상적으로 되었습니다.");
                }
            } else {
                if (isNowLiked) {
                    System.out.println("토글 실패: 좋아요가 정상적으로 되지 않았습니다.");
                } else {
                    System.out.println("토글 성공: 좋아요가 취소되었습니다.");
                }
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }
    }
}