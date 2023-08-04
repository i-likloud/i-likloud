package com.backend.api.mypage.dto;


import com.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class MypageInfoDto {

    private Long memberId;
    private String nickname;
    private int profileFace;
    private int profileColor;
    private int profileAccessory;


    public static MypageInfoDto of (Member member) {
        return MypageInfoDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileFace(member.getProfileFace())
                .profileColor(member.getProfileColor())
                .profileAccessory(member.getProfileAccessory())
                .build();
    }

}
