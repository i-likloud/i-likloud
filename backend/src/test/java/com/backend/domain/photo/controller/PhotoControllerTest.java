package com.backend.domain.photo.controller;


import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PhotoControllerTest extends BaseIntegrationTest {

    private String token = AccessToken.getNewToken();

    @Test
    @Rollback
    void uploadPhotos() throws Exception {
        //given
        ClassPathResource resource = new ClassPathResource("1915_20200114094530503.jpg");
        MockMultipartFile file = new MockMultipartFile("multipartFiles", resource.getFilename(), MediaType.IMAGE_JPEG_VALUE, resource.getInputStream());

        MockHttpServletRequestBuilder requestBuilder = multipart("/api/photo/upload")
                .file(file)
                .header("Authorization", "Bearer " + token);

        //when
        ResultActions resultActions = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //then
    }
}