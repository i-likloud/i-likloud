package com.backend.domain.photo.repository;

import com.backend.domain.photo.entity.Photo;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo,Long> {

    @Query("SELECT p FROM Photo p JOIN FETCH p.member ORDER BY p.createdAt DESC")
    List<Photo> findAllByOrderByCreatedAtDesc();

    @Query("SELECT p FROM Photo p JOIN FETCH p.member ORDER BY p.pickCnt DESC")
    List<Photo> findAllByOrderByPickCntDesc();

    @Query("SELECT p FROM Photo p JOIN FETCH p.member ORDER BY p.bookmarkCnt DESC")
    List<Photo> findAllByOrderByBookmarkCntDesc();

    List<Photo> findAllByMemberMemberId(Long memberId);

    // 즐겨찾기 증가
    @Transactional
    @Modifying
    @Query("UPDATE Photo p set p.bookmarkCnt = p.bookmarkCnt + 1 WHERE p.photoId = :photoId")
    void increaseBookmarkCount(@Param("photoId") Long photoId);

    // 즐겨찾기 감소
    @Transactional
    @Modifying
    @Query("UPDATE Photo p set p.bookmarkCnt = p.bookmarkCnt - 1 WHERE p.photoId = :photoId")
    void decreaseBookmarkCount(@Param("photoId") Long photoId);


}
