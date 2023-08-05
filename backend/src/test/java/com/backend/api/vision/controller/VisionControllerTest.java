package com.backend.api.vision.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class VisionControllerTest extends BaseIntegrationTest {

    private final String token = AccessToken.getNewToken();

    @Test
    @Rollback
    void extractLabels() throws Exception{
        //given
        ClassPathResource resource = new ClassPathResource("1915_20200114094530503.jpg");
        MockMultipartFile file = new MockMultipartFile("file", resource.getFilename(), MediaType.IMAGE_JPEG_VALUE, resource.getInputStream());

        MockHttpServletRequestBuilder requestBuilder = multipart("/api/vision/checkPhoto")
                .file(file)
                .header("Authorization", "Bearer " + token);

        //when
        ResultActions resultActions = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //then
    }

    @Test
    @Rollback
    void detectSafeSearch() throws Exception {
        //given
        ClassPathResource resource = new ClassPathResource("1915_20200114094530503.jpg");
        MockMultipartFile file = new MockMultipartFile("file", resource.getFilename(), MediaType.IMAGE_JPEG_VALUE, resource.getInputStream());

        MockHttpServletRequestBuilder requestBuilder = multipart("/api/vision/detectSafeSearch")
                .file(file)
                .header("Authorization", "Bearer " + token);

        //when
        ResultActions resultActions = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //then
    }
}