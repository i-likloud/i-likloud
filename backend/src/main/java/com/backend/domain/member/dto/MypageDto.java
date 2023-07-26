package com.backend.domain.member.dto;

import com.backend.domain.member.constant.ProfileColor;
import com.backend.domain.member.constant.ProfileFace;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MypageDto {
    private String email;
    private String nickname;
    private ProfileColor profileColor;
    private ProfileFace profileFace;

    @Builder
    public MypageDto(String email, String nickname, ProfileFace profileFace, ProfileColor profileColor) {
        this.email = email;
        this.nickname = nickname;
        this.profileFace = profileFace;
        this.profileColor = profileColor;
    }

    @Getter
    public static class UpdateRequest {
        private String email;
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
    }
}
