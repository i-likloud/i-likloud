package com.backend.api.photo.dto;

import com.backend.domain.photo.entity.Photo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PhotoWithBookmarkDto {
    private Long photoId;
    private String photoUrl;

    public PhotoWithBookmarkDto(Photo photo) {
        this.photoId = photo.getPhotoId();
        this.photoUrl = photo.getPhotoUrl();
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
