package com.backend.domain.bookmark.repository;

import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.member.entity.Member;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmarks, Long> {
    Optional<Bookmarks> findByMemberMemberIdAndPhotoPhotoId(Long memberId, Long photoId);
    List<Bookmarks> findByMember(Member member);

    // 북마크 일괄 삭제
    @Modifying
    @Query("DELETE FROM Bookmarks b WHERE b.photo.photoId = :photoId")
    void deleteBookmarksByPhotoId(@Param("photoId") Long photoId);

}

