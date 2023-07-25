package com.backend.domain.photo.repository;

import com.backend.domain.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo,Long> {


}
