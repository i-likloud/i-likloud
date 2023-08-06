package com.backend.domain.nft.repository;

import com.backend.domain.member.entity.Member;
import com.backend.domain.nft.entity.Nft;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NftRepository extends JpaRepository<Nft, Long> {
    Optional<Nft> findNftByDrawingDrawingId(Long drawingId);

    List<Nft> findByMember(Member member);

    Optional<Nft> findNftByNftId(Long nftId);

    @Modifying
    @Query("UPDATE Nft n SET n.drawing = NULL WHERE n.drawing.drawingId = :drawingId")
    void unlinkNftFromDrawing(@Param("drawingId") Long drawingId);
}
