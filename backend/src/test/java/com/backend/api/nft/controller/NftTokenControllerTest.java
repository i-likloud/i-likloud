package com.backend.api.nft.controller;

//class NftTokenControllerTest extends BaseIntegrationTest {
//
//    private String token = AccessToken.getToken();
//
//    @Test
//    @Rollback
//    void createWallet() {
//        String token = AccessToken.getToken();
//        try {
//            //when
//            ResultActions resultActions = mvc.perform(post("/api/nft/wallet")
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + token)) // "Authorization" 헤더에 토큰 추가
//                    .andDo(MockMvcResultHandlers.print());
//            //then
//            resultActions.andExpect(status().isOk());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Rollback
//    void createToken() {
//        Long drawingId = 1L;
//
//
//
//
//    }
//
//    @Test
//    @Rollback
//    void getTokenList() {
//    }
//
//    @Test
//    @Rollback
//    void getAllTokenList() {
//    }
//}