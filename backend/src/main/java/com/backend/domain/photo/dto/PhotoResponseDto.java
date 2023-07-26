package com.backend.domain.photo.dto;

import com.backend.domain.photo.entity.Photo;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PhotoResponseDto {

//    private Member memberId;
    private String photoUrl;
    private int pickCnt;

    public PhotoResponseDto(Photo photo) {
        this.photoUrl = photo.getPhotoUrl();
        this.pickCnt = photo.getPickCnt();
    }
//    @Builder
//    public PhotoDto(Member memberId, String photoUrl, int pickCnt) {
//        this.memberId = memberId;
//        this.photoUrl = photoUrl;
//        this.pickCnt = pickCnt;
//    }

//    public Photo toEntity() {
//        return Photo.builder()
//                .member(memberId)
//                .photoUrl(photoUrl)
//                .pickCnt(pickCnt)
//                .build();
//    }

}
