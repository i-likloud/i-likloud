package com.backend.oauth2.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoResponseDto {

    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    // account 안에 이메일, 프로필 있고 프로필안에 닉네임 프사 있음
    @Getter
    @Setter
    public static class KakaoAccount {

        private String email;
        private Profile profile;

        @Getter
        @Setter
        public static class Profile {
            private String nickname;

            @JsonProperty("profile_image_url")
            private String profileimageUrl;
        }
    }
}
