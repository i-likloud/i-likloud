package com.backend.external.nft.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class TransferDto {

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String status;
        private String transactionHash;
    }
}
