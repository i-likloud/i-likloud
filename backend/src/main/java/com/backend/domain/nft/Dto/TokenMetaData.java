package com.backend.domain.nft.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenMetaData {

    private String title;
    private String content;
    private String imageUrl;
    private String creator;
    private String owner;
    private String tokenId;

    public TokenMetaData(String title, String content, String imageUrl, String creator, String owner, String tokenId) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.creator = creator;
        this.owner = owner;
        this.tokenId = tokenId;
    }
}
