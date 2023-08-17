package com.backend.domain.photo.repository;

import com.backend.domain.drawing.entity.DrawingFile;
import com.backend.domain.photo.entity.PhotoFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoFileRepository extends JpaRepository<PhotoFile, Long> {

    Optional<PhotoFile> findFileByFileName(String filename);

}
