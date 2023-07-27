package com.backend.domain.member.dto;

import com.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class MypageInfoDto {
    private long memberId;
    private String nickname;
    private ProfileFace profileFace;
    private ProfileColor profileColor;
    private int coinCount;

    public static MypageInfoDto of (Member member) {
        return MypageInfoDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileFace(member.getProfileFace())
                .profileColor(member.getProfileColor())
                .coinCount(member.getCoinCount())
                .build();
    }

}
