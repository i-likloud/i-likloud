package com.backend.api.photo.dto;

import com.backend.domain.photo.entity.Photo;
import lombok.Getter;

@Getter
public class PhotoInfoResponseDto {

    private Long photoId;
    private Long memberId;
    private String photoUrl;

    public PhotoInfoResponseDto(Photo entity) {
        this.photoId = entity.getPhotoId();
        this.memberId = entity.getMember().getMemberId();
        this.photoUrl = entity.getPhotoUrl();

    }
}
