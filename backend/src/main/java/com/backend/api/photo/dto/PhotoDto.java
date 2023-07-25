package com.backend.api.photo.dto;

import com.backend.domain.member.entity.Member;
import com.backend.domain.photo.entity.Photo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PhotoDto {

    private Member memberId;
    private String photoUrl;
    private int pickCnt;

    @Builder
    public PhotoDto(Member memberId, String photoUrl, int pickCnt) {
        this.memberId = memberId;
        this.photoUrl = photoUrl;
        this.pickCnt = pickCnt;
    }

    public Photo toEntity() {
        return Photo.builder()
                .memberId(memberId)
                .photoUrl(photoUrl)
                .pickCnt(pickCnt)
                .build();
    }

}
