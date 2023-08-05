package com.backend.domain.photo.repository;

import com.backend.domain.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo,Long> {

    @Query("SELECT p FROM Photo p JOIN FETCH p.member JOIN FETCH p.photoFile")
    List<Photo> findAllByOrderByCreatedAtDesc();

    @Query("SELECT p FROM Photo p JOIN FETCH p.member JOIN FETCH p.photoFile")
    List<Photo> findAllByOrderByPickCntDesc();

    @Query("SELECT p FROM Photo p JOIN FETCH p.member JOIN FETCH p.photoFile")
    List<Photo> findAllByOrderByBookmarkCntDesc();

    List<Photo> findAllByMemberMemberId(Long memberId);


}
