package com.backend.domain.likes.repository;

import com.backend.domain.likes.entity.Likes;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByMemberMemberIdAndDrawingDrawingId(Long memberId, Long drawingId);

    Optional<Likes> findByMemberMemberIdAndDrawingDrawingId(Long memberId, Long drawingId);

    Optional<Likes> findByMemberMemberId(Long memberId);

    List<Likes> findAllByMemberMemberId(Long memberId);

    @Query("select l.drawing.drawingId from Likes l where l.member.memberId = :memberId and l.drawing.drawingId in :drawingIds")
    Set<Long> findLikedDrawingIdsByMember(@Param("memberId") Long memberId, @Param("drawingIds") List<Long> drawingIds);
}
