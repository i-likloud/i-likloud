package com.backend.domain.member.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.global.jwt.dto.JwtDto;
import com.backend.global.util.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true, nullable = false, length = 20)
    private String email;

    @Column(length = 30)
    private String nickname;

    private String profile_face;

    private String profile_color;

    private String wallet;

    private int coin_cnt;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(length = 250)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime tokenExpirationTime;


    //== 멤버 필드 업데이트 ==//
    public void updateNickname(String updateNickname) {
        this.nickname = updateNickname;
    }

    public void updateProfileCOLOR(String profile_color){
        this.profile_color = profile_color;
    }

    public void updateProfileFace(String profile_face){
        this.profile_face = profile_face;
    }


    public void updateCoin_cnt(int coin_cnt){
        this.coin_cnt = coin_cnt;
    }

    public void updateRole(Role role){
        this.role = role;
    }

    public void updateRefreshToken(JwtDto jwtDto) {
        this.refreshToken = jwtDto.getRefreshToken();
        this.tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(jwtDto.getRefreshTokenExpirationPeriod());
    }

    public void expireRefreshToken(LocalDateTime now) {
        this.tokenExpirationTime = now;
    }

    @Builder
    public Member(SocialType socialType, String email, String nickname,  int coin_cnt,
                  String profile_color, String profile_face,Role role) {
        this.socialType = socialType;
        this.email = email;
        this.nickname = nickname;
        this.profile_color = profile_color;
        this.profile_face = profile_face;
        this.coin_cnt = coin_cnt;
        this.role = role;
    }




}
