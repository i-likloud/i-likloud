package com.backend.domain.drawing.repository;

import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrawingRepository extends JpaRepository<Drawing, Long> {

    List<Drawing> findDrawingByMember_MemberId(Long memberId);

    @Query("SELECT d FROM Drawing d JOIN FETCH d.member ORDER BY d.createdAt DESC")
    List<Drawing> findAllWithMemberByCreatedAtDesc();

    List<Drawing> findByPhotoOrderByLikesCountDesc(Photo photo);
}
