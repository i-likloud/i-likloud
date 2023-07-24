package com.backend.api.member.dto;

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

    private String profile_image;

    private String phone_number;

    private SocialType socialType;

    private Role role;

    public static MemberInfoResponseDto of(Member member) {
        return MemberInfoResponseDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profile_image(member.getProfile_image())
                .phone_number(member.getPhone_number())
                .role(member.getRole())
                .socialType(member.getSocialType())
                .build();
    }
}
