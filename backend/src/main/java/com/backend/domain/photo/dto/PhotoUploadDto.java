package com.backend.domain.photo.dto;

import com.backend.domain.photo.entity.Photo;
import lombok.Getter;

@Getter
public class PhotoUploadDto {

    private final Long photoId;
    private final String photoUrl;
    private final Long memberId;
    private final Long photoFileId;

    public PhotoUploadDto(Photo photo, Long memberId, Long photoFileId) {
        this.photoId = photo.getPhotoId();
        this.photoUrl = photo.getPhotoUrl();
        this.memberId = memberId;
        this.photoFileId = photoFileId;
    }
}
