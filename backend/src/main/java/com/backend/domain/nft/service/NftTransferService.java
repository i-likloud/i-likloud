package com.backend.domain.nft.service;

import com.backend.domain.nft.entity.NftTransfer;
import com.backend.domain.nft.repository.NftTransferRepository;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NftTransferService {

    private final NftTransferRepository nftTransferRepository;


    public NftTransfer findTransferById(Long transferId) {
        return nftTransferRepository.findById(transferId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_TRANSFER));

    }
}
