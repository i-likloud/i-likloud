package com.backend.api.mypage.dto;

import com.backend.domain.likes.entity.Likes;
import com.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class MypageInfoDto {
    private long memberId;
    private String nickname;
    private int profileFace;
    private int profileColor;
    private int profileAccessory;
    private int goldCoin;
    private int silverCoin;
    private List<Likes> likes;

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
                .build();
    }


}
