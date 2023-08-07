package com.backend.api.member.dto;

import com.backend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberFindDto {

    private Long memberId;
    private String nickname;
    private int profileColor;
    private int profileFace;
    private int profileAccessory;

    @Builder
    public MemberFindDto(Long memberId,String nickname, int profileFace,
                           int profileColor, int profileAccessory) {
        this. memberId = memberId;
        this.nickname = nickname;
        this.profileFace = profileFace;
        this.profileColor = profileColor;
        this.profileAccessory = profileAccessory;

    }

    public static MemberFindDto of(Member member) {
        return MemberFindDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileFace(member.getProfileFace())
                .profileColor(member.getProfileColor())
                .profileAccessory(member.getProfileAccessory())
                .build();
    }
}
