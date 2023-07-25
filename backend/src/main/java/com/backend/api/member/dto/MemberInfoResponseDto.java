package com.backend.api.member.dto;

import com.backend.domain.member.constant.ProfileColor;
import com.backend.domain.member.constant.ProfileFace;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponseDto {
    // 회원 정보 조회 Dto

    private Long memberId;

    private String email;

    private String nickname;

    private ProfileFace profileFace;
    private ProfileColor profileColor;
    private int coinCount;

    private SocialType socialType;

    private Role role;

    public static MemberInfoResponseDto of(Member member) {
        return MemberInfoResponseDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileFace(member.getProfileFace())
                .profileColor(member.getProfileColor())
                .role(member.getRole())
                .socialType(member.getSocialType())
                .build();
    }
}
