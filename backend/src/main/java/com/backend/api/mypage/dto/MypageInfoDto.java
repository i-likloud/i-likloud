package com.backend.api.mypage.dto;

import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.likes.entity.Likes;
import com.backend.domain.member.entity.Member;
import com.backend.domain.photo.entity.Photo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class MypageInfoDto {
    private Long memberId;
    private String nickname;
    private int profileFace;
    private int profileColor;
    private int profileAccessory;
    private int goldCoin;
    private int silverCoin;
    private List<Likes> likes;
    private List<Photo> photos;
    private List<Drawing> drawings;

    public static MypageInfoDto of (Member member) {
        return MypageInfoDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileFace(member.getProfileFace())
                .profileColor(member.getProfileColor())
                .profileAccessory(member.getProfileAccessory())
                .goldCoin(member.getGoldCoin())
                .silverCoin(member.getSilverCoin())
                .likes(member.getLikes())
                .photos(member.getPhotos())
                .drawings(member.getDrawings())
                .build();
    }

}
