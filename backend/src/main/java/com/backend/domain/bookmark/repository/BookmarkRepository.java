package com.backend.domain.bookmark.repository;

import com.backend.domain.bookmark.entity.Bookmarks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmarks, Long> {
    Optional<Bookmarks> findByMemberMemberIdAndPhotoPhotoId(Long memberId, Long photoId);
}

