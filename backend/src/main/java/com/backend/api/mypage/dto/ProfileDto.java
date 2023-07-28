package com.backend.api.mypage.dto;

import com.backend.domain.member.entity.Member;
import lombok.*;

@Getter
@Setter
@Builder
public class ProfileDto {
    private long memberId;
    private String nickname;
    private int profileFace;
    private int profileColor;
    private int profileAccessory;
    private int goldCoin;
    private int silverCoin;

    public static ProfileDto of (Member member) {
        return ProfileDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileFace(member.getProfileFace())
                .profileColor(member.getProfileColor())
                .profileAccessory(member.getProfileAccessory())
                .goldCoin(member.getGoldCoin())
                .silverCoin(member.getSilverCoin())
                .build();
    }

    @Getter
    public static class editRequest{
        private String nickname;
        private int profileColor;
        private int profileFace;
        private int profileAccessory;
    }
}
