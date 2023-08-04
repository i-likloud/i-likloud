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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StoreControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private String token = AccessToken.getToken();
    private String userEmail = AccessToken.getTestEmail();


    @Test
    @Rollback
    void getStore() throws Exception{
        //given

        //when
        try {
            ResultActions resultActions = mvc.perform(get("/api/store/home")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions
                    .andExpect(status().isOk());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

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
                Long storeId = Long.parseLong(createResponse);


            //when
            ResultActions resultActions = mvc.perform(post("/api/store/buy/{storeId}", storeId)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
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
    void uploadAccessory() {

        // given
        String accessoryName = "새로운 아이템";
        int accessoryPrice = 0;

        AccessoryUploadRequestDto requestDto = new AccessoryUploadRequestDto();
        requestDto.setAccessoryName(accessoryName);
        requestDto.setAccessoryPrice(accessoryPrice);

        try {
            //when
            ResultActions resultActions = mvc.perform(post("/api/store/accessoryUpload")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(new ObjectMapper().writeValueAsString(requestDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());

            //then
            resultActions.andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}