package com.backend.domain.history.repository;

import com.backend.domain.comment.entity.Comment;
import com.backend.domain.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByMemberMemberId(Long memberId);
}
