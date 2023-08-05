package com.backend.api.nft.dto;

import com.backend.api.mypage.dto.MypageInfoDto;
import com.backend.domain.nft.entity.NftTransfer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NftGiftDto {

    private Long transferId;
    private MypageInfoDto mypageInfoDto;
    private NftListResponseDto nftListResponseDto;
    private String message;

    public NftGiftDto (NftTransfer nftTransfer, MypageInfoDto mypageInfoDto, NftListResponseDto nftListResponseDto){
        this.message = nftTransfer.getMessage();
        this.transferId = nftTransfer.getTransferId();
        this.mypageInfoDto = mypageInfoDto;
        this.nftListResponseDto = nftListResponseDto;
    }
}
