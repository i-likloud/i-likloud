package com.backend.domain.member.dto;

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

    public static ProfileDto of (Member member) {
        return ProfileDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileFace(member.getProfileFace())
                .profileColor(member.getProfileColor())
                .build();
    }

    @Getter
    public static class editRequest{
        private String nickname;
        private int profileColor;
        private int profileFace;
    }
}
