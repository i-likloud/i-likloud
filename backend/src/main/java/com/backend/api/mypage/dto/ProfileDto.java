package com.backend.api.mypage.dto;

import com.backend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "예시 DTO")
    public static class editRequest{
        @Schema(example = "2")
        private int profileColor;
        @Schema(example = "2")
        private int profileFace;
        @Schema(example = "")
        private int profileAccessory;
    }
}
