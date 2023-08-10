package com.backend.api.comment.controller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTestEx extends BaseIntegrationTest {

    private String token = AccessToken.getNewToken();

    @Test
    @Rollback  //404확인
    void createCommentWrongDrawingId() throws Exception {

        // given
        Long drawingId = 9999999999L;
        String content = "댓글 내용";

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/comment/to/{drawingId}", drawingId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer "+token)
                            .param("content", content)

                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions.andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Test
    @Rollback //404확인
    void createAndDeleteCommentWrongCommentId() {
        String token = AccessToken.getNewToken();

        // given
        Long commentId = 9999999999L;
        String content = "댓글 내용";

        try {
            // 댓글 삭제
            ResultActions deleteResult = mvc.perform(delete("/api/comment/delete/{commentId}", commentId)
                            .header("Authorization", "Bearer " + token))
                    .andDo(MockMvcResultHandlers.print());

            // then
            deleteResult.andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}