package com.backend.api.nft.dto;

import com.backend.domain.nft.entity.Nft;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NftListResponseDto {

    private String owner;
    private Long nftId;
    private String imageUrl;
    private String title;
    private String content;

    public NftListResponseDto(Nft nft) {
        this.owner = nft.getMember().getNickname();
        this.title = nft.getDrawing().getTitle();
        this.nftId = nft.getNftId();
        this.imageUrl = nft.getNftImageUrl();
        this.content = nft.getDrawing().getContent();
    }
}
