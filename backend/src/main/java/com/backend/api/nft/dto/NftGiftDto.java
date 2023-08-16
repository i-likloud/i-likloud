package com.backend.api.nft.dto;

import com.backend.api.mypage.dto.MypageInfoDto;
import com.backend.domain.nft.entity.Nft;
import com.backend.domain.nft.entity.NftTransfer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NftGiftDto {

    private Long transferId;
    private MypageInfoDto mypageInfoDto;
    private final Long nftId;
    private final String imageUrl;
    private final String title;
    private final String content;
    private final String tokenId;
    private String message;

    public NftGiftDto (NftTransfer nftTransfer, MypageInfoDto mypageInfoDto, Nft nft){
        this.message = nftTransfer.getMessage();
        this.transferId = nftTransfer.getTransferId();
        this.mypageInfoDto = mypageInfoDto;
        this.nftId = nft.getNftId();
        this.tokenId = nft.getTokenId();
        this.title = nft.getTitle();
        this.imageUrl = nft.getNftImageUrl();
        this.content = nft.getContent();
    }
}
