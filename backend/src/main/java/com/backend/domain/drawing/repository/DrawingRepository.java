package com.backend.domain.drawing.repository;

import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DrawingRepository extends JpaRepository<Drawing, Long> {

    List<Drawing> findDrawingByMember_MemberId(Long memberId);
    List<Drawing> findDrawingByPhoto_PhotoId(Long photoId);

    List<Drawing> findByPhotoOrderByLikesCountDesc(Photo photo);
}
