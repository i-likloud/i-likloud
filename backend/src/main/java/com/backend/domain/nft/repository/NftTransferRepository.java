package com.backend.domain.nft.repository;

import com.backend.domain.nft.entity.NftTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NftTransferRepository extends JpaRepository<NftTransfer, Long> {
}
