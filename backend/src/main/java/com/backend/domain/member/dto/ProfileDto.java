package com.backend.domain.member.dto;

import com.backend.domain.member.constant.ProfileColor;
import com.backend.domain.member.constant.ProfileFace;
import com.backend.domain.member.entity.Member;
import lombok.*;

@Getter
@Setter
@Builder
public class ProfileDto {
    private long memberId;
    private String nickname;
    private ProfileFace profileFace;
    private ProfileColor profileColor;

    public static ProfileDto of (Member member) {
        return ProfileDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileFace(member.getProfileFace())
                .profileColor(member.getProfileColor())
                .build();
    }
}
