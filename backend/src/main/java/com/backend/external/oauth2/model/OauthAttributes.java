package com.backend.external.oauth2.model;

import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter @Builder
public class OauthAttributes { // 회원 정보 가져올 때 통일시킴

    private String nickname;
    private String email;
    private SocialType socialType;

    public Member toMemberEntity(SocialType socialType) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .socialType(socialType)
                .role(Role.GUEST)
                .build();
    }

}
