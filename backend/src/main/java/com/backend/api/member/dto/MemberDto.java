package com.backend.api.member.dto;

import com.backend.domain.member.constant.ProfileColor;
import com.backend.domain.member.constant.ProfileFace;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private String email;
    private String nickname;
    private ProfileColor profileColor;
    private ProfileFace profileFace;
    private int coinCount;

    @Builder
    public MemberDto(String email, String nickname, ProfileFace profileFace, ProfileColor profileColor, int coinCount) {
        this.email = email;
        this.nickname = nickname;
        this.profileFace = profileFace;
        this.profileColor = profileColor;
        this.coinCount = coinCount;

    }

    @Getter
    public static class UpdateRequest {
        private String nickname;
        private ProfileColor profileColor;
        private ProfileFace profileFace;
    }


    @Getter
    @Builder
    public static class Response {
        private String email;
        private String nickname;
        private ProfileColor profileColor;
        private ProfileFace profileFace;
        private SocialType socialType;
        private Role role;

    }
}
