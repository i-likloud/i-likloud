package com.backend.api.photo.dto;

import com.backend.domain.photo.entity.Photo;
import lombok.Getter;

@Getter
public class PhotoInfoResponseDto {

    private final Long photoId;
    private final Long memberId;
    private final String photoUrl;
    private final boolean memberBookmarked;
    private final int pickCount;
    private final int bookmarkCount;

    public PhotoInfoResponseDto(Photo entity, boolean memberBookmarked) {
        this.photoId = entity.getPhotoId();
        this.memberId = entity.getMember().getMemberId();
        this.photoUrl = entity.getPhotoUrl();
        this.memberBookmarked = memberBookmarked;
        this.pickCount = entity.getPickCnt();
        this.bookmarkCount = entity.getBookmarkCnt();

    }
}
