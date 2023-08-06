package com.backend.domain.drawing.repository;

import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.photo.entity.Photo;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrawingRepository extends JpaRepository<Drawing, Long> {

    List<Drawing> findDrawingByMember_MemberId(Long memberId);

    @Query("SELECT d FROM Drawing d JOIN FETCH d.member ORDER BY d.createdAt DESC")
    List<Drawing> findAllWithMemberByCreatedAtDesc();

    List<Drawing> findByPhotoOrderByLikesCountDesc(Photo photo);

    @Modifying // DB 내용 수정 표시
    @Query("UPDATE Drawing d SET d.photo = NULL WHERE d.photo.photoId = :photoId")
    void unlinkDrawingsFromPhoto(@Param("photoId") Long photoId);
}
