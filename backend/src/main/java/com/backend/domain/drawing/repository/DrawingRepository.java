package com.backend.domain.drawing.repository;

import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.photo.entity.Photo;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DrawingRepository extends JpaRepository<Drawing, Long> {

    List<Drawing> findDrawingByMember_MemberId(Long memberId);

    @Query("SELECT d FROM Drawing d JOIN FETCH d.member ORDER BY d.createdAt DESC")
    List<Drawing> findAllWithMemberByCreatedAtDesc();

    List<Drawing> findByPhotoOrderByLikesCountDesc(Photo photo);

    // 사진 삭제시 관련 그림의 사진 null 처리
    @Modifying // DB 내용 수정 표시
    @Query("UPDATE Drawing d SET d.photo = NULL WHERE d.photo.photoId = :photoId")
    void unlinkDrawingsFromPhoto(@Param("photoId") Long photoId);

    // NFT 소멸시 관련 그림의 NFT null 처리
    @Modifying
    @Query("UPDATE Drawing d SET d.nft = NULL WHERE d.nft.nftId = :nftId")
    void unlinkDrawingFromNft(@Param("nftId") Long nftId);

    // 댓글이랑 같이 그림 가져오기
    @Query("SELECT d FROM Drawing d LEFT JOIN FETCH d.comments WHERE d.drawingId = :drawingId")
    Optional<Drawing> findByDrawingIdWithComments(@Param("drawingId") Long drawingId);

    // 좋아요 증가
    @Transactional
    @Modifying
    @Query("UPDATE Drawing d set d.likesCount = d.likesCount + 1 WHERE d.drawingId = :drawingId")
    void increaseLikesCount(@Param("drawingId") Long drawingId);

    // 좋아요 감소
    @Transactional
    @Modifying
    @Query("UPDATE Drawing d set d.likesCount = d.likesCount - 1 WHERE d.drawingId = :drawingId")
    void decreaseLikesCount(@Param("drawingId") Long drawingId);
}
