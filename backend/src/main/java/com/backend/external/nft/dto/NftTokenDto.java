package com.backend.external.nft.dto;

import lombok.Builder;
import lombok.Getter;


public class NftTokenDto {

    @Builder
    @Getter
    public static class Request {
        // 발행 토큰 소유자
        private String to;
        // 토큰 id
        private String id;
        // 토큰uri (그림URL)
        private String uri;
    }

    @Getter
    @Builder
    public static class Response {
        private String status;
        private String transactionHash;
    }

}