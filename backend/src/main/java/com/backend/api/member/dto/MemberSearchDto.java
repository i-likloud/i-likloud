package com.backend.api.member.dto;

import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSearchDto {


    private String nickname;
    @Schema(example = "1")
    private int profileColor;
    private int profileFace;
    private int profileAccessory;


    @Builder
    public MemberSearchDto(String nickname, int profileFace,
                           int profileColor, int profileAccessory) {

        this.nickname = nickname;
        this.profileFace = profileFace;
        this.profileColor = profileColor;
        this.profileAccessory = profileAccessory;

    }


    public static MemberSearchDto of(Member member) {
            return MemberSearchDto.builder()
                    .nickname(member.getNickname())
                    .profileFace(member.getProfileFace())
                    .profileColor(member.getProfileColor())
                    .profileAccessory(member.getProfileAccessory())
                    .build();
        }
    }

