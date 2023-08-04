package com.backend.domain.nft.repository;

import com.backend.domain.nft.entity.NftTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NftTransferRepository extends JpaRepository<NftTransfer, Long> {

    List<NftTransfer> findByToMember(Long toMemberId);

    void deleteById(Long transferId);
}