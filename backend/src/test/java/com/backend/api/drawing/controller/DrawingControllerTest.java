package com.backend.api.drawing.controller;


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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DrawingControllerTest extends BaseIntegrationTest {

    private String token = AccessToken.getNewToken();


    @Test
    @Rollback
    void uploadDrawing() throws Exception {
        //given
        Long photoId = 1L;

        ClassPathResource resource = new ClassPathResource("1915_20200114094530503.jpg");
        MockMultipartFile file = new MockMultipartFile("file", resource.getFilename(), MediaType.IMAGE_JPEG_VALUE, resource.getInputStream());

        String title = "제목";
        String content = "내용";

        MockPart titlePart = new MockPart("title", title.getBytes());
        MockPart contentPart = new MockPart("content", content.getBytes());

        MockHttpServletRequestBuilder requestBuilder = multipart("/api/drawings/upload/from/{photoId}", photoId)
                .file(file)
                .part(titlePart)      // title을 멀티파트로 전송
                .part(contentPart)  // content를 멀티파트로 전송
                .header("Authorization", "Bearer " + token);

        //when

        ResultActions resultActions = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //then

    }

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