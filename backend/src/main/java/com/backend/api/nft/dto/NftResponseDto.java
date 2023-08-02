package com.backend.api.nft.dto;

import com.backend.domain.nft.entity.Nft;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NftResponseDto {
    private Long nftId;
    private String metadata;
    private String imageUrl;
    private Long ownerId;
    private Long drawingId;

    public NftResponseDto(Nft nft){
        this.nftId = nft.getNftId();
        this.metadata = nft.getNftMetadata();
        this.imageUrl = nft.getNftImageUrl();
        this.ownerId = nft.getMember().getMemberId();
        this.drawingId = nft.getDrawing().getDrawingId();
    }
}
