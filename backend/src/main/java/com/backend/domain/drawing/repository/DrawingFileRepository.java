package com.backend.domain.drawing.repository;

import com.backend.domain.drawing.entity.DrawingFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrawingFileRepository extends JpaRepository<DrawingFile, Long> {

    Optional<DrawingFile> findFileByFileName(String filename);


}
