package com.backend.domain.photo.service;

import com.backend.api.photo.dto.PhotoDto;
import com.backend.domain.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PhotoService {
    private final PhotoRepository photoRepository;

    @Transactional
    public Long register(PhotoDto requestDto) {
        return photoRepository.save(requestDto.toEntity()).getPhotoId();
    }
}
