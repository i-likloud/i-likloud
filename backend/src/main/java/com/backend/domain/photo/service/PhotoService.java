package com.backend.domain.photo.service;

import com.backend.api.photo.dto.PhotoDto;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
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

    public Photo findPhotoByPhotoId(Long photoId){
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_FILE));
    }
}
