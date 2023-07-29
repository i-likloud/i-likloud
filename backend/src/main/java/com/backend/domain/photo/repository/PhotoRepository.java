package com.backend.domain.photo.repository;

import com.backend.domain.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo,Long> {

    List<Photo> findAllByOrderByCreatedAtDesc();

    List<Photo> findAllByOrderByPickCntDesc();

    List<Photo> findAllByOrderByBookmarkCntDesc();

    List<Photo> findAllByMemberMemberId(Long memberId);
}
