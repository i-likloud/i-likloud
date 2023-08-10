package com.backend.api.store.conotroller;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.accessory.dto.AccessoryUploadRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StoreControllerTestEx extends BaseIntegrationTest {


    private String token = AccessToken.getNewToken();



    @Test
    @Rollback
    void buyAccessory() throws Exception{

        // given
//        Long storeId = 1L;

        try {
            // 댓글 생성
            // given
            String accessoryName = "새로운 아이템";
            int accessoryPrice = 0;

            AccessoryUploadRequestDto requestDto = new AccessoryUploadRequestDto();
            requestDto.setAccessoryName(accessoryName);
            requestDto.setAccessoryPrice(accessoryPrice);
            
                //when
                ResultActions createResult = mvc.perform(post("/api/store/accessoryUpload")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(new ObjectMapper().writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

                //then
                createResult.andExpect(status().isCreated());

                // 생성된 댓글의 ID 받아오기
                String createResponse = createResult.andReturn().getResponse().getContentAsString();
                Long storeId = 9999L;


            //when
            ResultActions resultActions = mvc.perform(post("/api/store/buy/{storeId}", storeId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .param("storeId","9999")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions.andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}