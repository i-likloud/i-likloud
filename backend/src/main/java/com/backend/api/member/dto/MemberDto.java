package com.backend.api.member.dto;

import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private String email;
    private String nickname;
    private int profileColor;
    private int profileFace;
    private int profileAccessory;
    private int goldCoin;
    private int silverCoin;

    @Builder
    public MemberDto(String email, String nickname, int profileFace,
                     int profileColor, int profileAccessory, int goldCoin, int silverCoin) {
        this.email = email;
        this.nickname = nickname;
        this.profileFace = profileFace;
        this.profileColor = profileColor;
        this.profileAccessory = profileAccessory;
        this.goldCoin = goldCoin;
        this.silverCoin = silverCoin;
    }

    @Getter
    public static class UpdateRequest {
        private String nickname;
        private int profileColor;
        private int profileFace;
        private int profileAccessory;
    }


    @Getter
    @Builder
    public static class Response {
        private String email;
        private String nickname;
        private int profileColor;
        private int profileFace;
        private int profileAccessory;
        private SocialType socialType;
        private Role role;

    }
}
