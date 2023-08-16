package com.backend.api.nft.dto;

import com.backend.domain.nft.entity.NftTransfer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NftTransferResponseDto {

    private final Long transferId;
    private final Long toMember;
    private final Long fromMember;
    private final String tokenId;
    private final String message;
    private final Long nftId;

    public NftTransferResponseDto(NftTransfer nftTransfer) {
        this.transferId = nftTransfer.getTransferId();
        this.toMember = nftTransfer.getToMember();
        this.fromMember = nftTransfer.getFromMember();
        this.tokenId = nftTransfer.getTokenId();
        this.message = nftTransfer.getMessage();
        this.nftId = nftTransfer.getNft().getNftId();
    }
}
