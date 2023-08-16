package com.backend.external.nft.dto;

import lombok.Builder;
import lombok.Getter;

public class WalletDto {

    @Builder
    @Getter
    public static class Request {
        private String accessKeyId;
        private String accessKeySecret;
        private String xChainId;
        private String authorizationHeader;
    }

    @Getter
    public static class Response {
        private String address;
        private String keyId;
        private String krn;
    }
}
