package com.backend.domain.likes.repository;

import com.backend.domain.likes.entity.Likes;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByMemberMemberIdAndDrawingDrawingId(Long memberId, Long drawingId);

    List<Likes> findAllByMemberMemberId(Long memberId);

    // 멤버가 좋아요한 그림게시물 id 찾기
    @Query("select l.drawing.drawingId from Likes l where l.member.memberId = :memberId and l.drawing.drawingId in :drawingIds")
    Set<Long> findLikedDrawingIdsByMember(@Param("memberId") Long memberId, @Param("drawingIds") List<Long> drawingIds);

    // 좋아요 일괄 삭제
    @Modifying
    @Query("DELETE FROM Likes l WHERE l.drawing.drawingId = :drawingId")
    void deleteLikesByDrawingId(@Param("drawingId") Long drawingId);

    void deleteByMemberMemberIdAndDrawingDrawingId(Long memberId, Long drawingId);

}
