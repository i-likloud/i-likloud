package com.backend.domain.likes.repository;

import com.backend.domain.likes.entity.Likes;
import com.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByMemberMemberIdAndDrawingDrawingId(Long memberId, Long drawingId);

    Optional<Likes> findByMemberMemberIdAndDrawingDrawingId(Long memberId, Long drawingId);
}
