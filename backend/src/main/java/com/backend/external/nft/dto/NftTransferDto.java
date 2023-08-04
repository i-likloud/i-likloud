package com.backend.external.nft.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NftTransferDto {


    @Getter
    @Builder
    @Setter
    public static class Request {
        private String sender;
        private String owner;
        private String to;
    }



    @Getter
    @Setter
    @Builder
    public static class Response {
        private String status;
        private String transactionHash;
    }
}
