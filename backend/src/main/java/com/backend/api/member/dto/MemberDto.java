package com.backend.api.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private String email;
    private String nickname;
    private String profile_image;
    private String phone_number;

    @Builder
    public MemberDto(String email, String nickname, String profile_image, String phone_number) {
        this.email = email;
        this.nickname = nickname;
        this.profile_image = profile_image;
        this.phone_number = phone_number;
    }

    @Getter
    public static class UpdateRequest {
        private String email;
        private String nickname;
        private String profile_image;
        private String phone_number;
    }


    @Getter
    @Builder
    public static class Response {
        private String email;
        private String nickname;
        private String profile_image;
        private String phone_number;
    }
}
