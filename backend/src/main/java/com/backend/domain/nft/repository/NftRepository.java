package com.backend.domain.nft.repository;

import com.backend.domain.member.entity.Member;
import com.backend.domain.nft.entity.Nft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NftRepository extends JpaRepository<Nft, Long> {
    Optional<Nft> findNftByDrawingDrawingId(Long drawingId);

    List<Nft> findByMember(Member member);

    Optional<Nft> findNftByNftId(Long nftId);
}
