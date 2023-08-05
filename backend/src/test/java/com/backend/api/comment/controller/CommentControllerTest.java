package com.backend.api.comment.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends BaseIntegrationTest {

    private AccessToken accessToken;
    private String token = AccessToken.getNewToken();
    private String userEmail = AccessToken.getTestEmail();

    @Test
    @Rollback
    void createComment() throws Exception {

        // given
        Long drawingId = 1L;
        String content = "댓글 내용";

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/comment/to/{drawingId}", drawingId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .param("content", content)

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
    void createAndDeleteComment() {
        // given
        Long drawingId = 1L;
        String content = "댓글 내용";

        try {
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
            Long commentId = Long.parseLong(createResponse);

            // 댓글 삭제
            ResultActions deleteResult = mvc.perform(delete("/api/comment/delete/{commentId}", commentId)
                            .header("Authorization", "Bearer " + token))
                    .andDo(MockMvcResultHandlers.print());

            // then
            deleteResult.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}