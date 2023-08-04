package com.backend.external.nft.dto;

import lombok.Builder;
import lombok.Getter;

public class DeleteTokenDto {

    @Getter
    @Builder
    public static class Request {
        private String from;
    }

    @Getter
    @Builder
    public static class Response {
        private String status;
        private String transactionHash;
    }
}
