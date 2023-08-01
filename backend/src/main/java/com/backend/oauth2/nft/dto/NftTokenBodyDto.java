package com.backend.oauth2.nft.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NftTokenBodyDto {
    
    // 받을 사람
    private String to;
    // 0x + 드로잉id
    private String id;
    // 사진 주소 값
    private String uri;

    // Add constructors, getters, and setters if needed

}