package com.backend.domain.nft.service;

import com.backend.domain.nft.entity.Nft;
import com.backend.domain.nft.repository.NftRepository;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NftService {

    private final NftRepository nftRepository;

    public Nft findNftByDrawingId(Long drawingId){
        return nftRepository.findNftByDrawingDrawingId(drawingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_NFT));
    }

    public Nft findNftById(Long nftId){
        return nftRepository.findNftByDrawingDrawingId(nftId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_NFT));
    }
}
