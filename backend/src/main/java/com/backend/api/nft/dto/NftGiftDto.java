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

    public NftGiftDto (NftTransfer nftTransfer){
        this.message = nftTransfer.getMessage();


    }
}
