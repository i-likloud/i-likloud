package com.backend.domain.bookmark.repository;

import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmarks, Long> {
    Optional<Bookmarks> findByMemberMemberIdAndPhotoPhotoId(Long memberId, Long photoId);
    List<Bookmarks> findByMember(Member member);

    boolean existsByMemberMemberIdAndPhotoPhotoId(Long memberId, Long PhotoId);

}

