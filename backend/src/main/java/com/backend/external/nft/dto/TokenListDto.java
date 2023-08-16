package com.backend.external.nft.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TokenListDto {

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String cursor;
        private List<Item> items;

    }
}

