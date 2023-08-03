package com.backend.api.photo.dto;

import com.backend.domain.photo.entity.Photo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PhotoInfoResponseDto {

    private final Long photoId;
    private final Long memberId;
    private final String photoUrl;
    private final int pickCount;
    private final int bookmarkCount;

    public PhotoInfoResponseDto(Photo entity) {
        this.photoId = entity.getPhotoId();
        this.memberId = entity.getMember().getMemberId();
        this.photoUrl = entity.getPhotoUrl();
        this.pickCount = entity.getPickCnt();
        this.bookmarkCount = entity.getBookmarkCnt();
    }

    @JsonCreator
    public PhotoInfoResponseDto(@JsonProperty("photoId") Long photoId, @JsonProperty("memberId") Long memberId, @JsonProperty("photoUrl") String photoUrl,
                          @JsonProperty("pickCount") int pickCount, @JsonProperty("bookmarkCount") int bookmarkCount) {
        this.photoId = photoId;
        this.memberId = memberId;
        this.photoUrl = photoUrl;
        this.pickCount = pickCount;
        this.bookmarkCount = bookmarkCount;
    }

}
