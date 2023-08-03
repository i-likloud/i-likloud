package com.backend.api.photo.dto;

import com.backend.domain.photo.entity.Photo;
import lombok.Getter;

@Getter
public class PhotoDetailDto extends PhotoInfoResponseDto{

    private final boolean memberBookmarked;
    private final int pickCount;
    private final int bookmarkCount;


    public PhotoDetailDto(Photo photo, boolean memberBookmarked) {
        super(photo);
        this.memberBookmarked = memberBookmarked;
        this.pickCount = photo.getPickCnt();
        this.bookmarkCount = photo.getBookmarkCnt();
    }
}
