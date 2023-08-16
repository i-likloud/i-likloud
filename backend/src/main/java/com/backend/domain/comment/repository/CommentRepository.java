package com.backend.domain.comment.repository;

import com.backend.domain.comment.entity.Comment;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.drawing.drawingId = :drawingId")
    void deleteCommentByDrawingId(@Param("drawingId") Long drawingId);
}
