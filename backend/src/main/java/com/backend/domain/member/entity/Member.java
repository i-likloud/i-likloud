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

    private String password;

    @Column(length = 30)
    private String nickname;

    private String profile_image;

    @Column(length = 20)
    private String phone_number;

    private String wallet;

    private int stamp_cnt;

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

    public void updateProfileImage(String profile_image){
        this.profile_image = profile_image;
    }
    public void updatePhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public void updateStamp_cnt(int stamp_cnt){
        this.stamp_cnt = stamp_cnt;
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
    public Member(SocialType socialType, String email, String password, String nickname, String phone_number, int stamp_cnt,
                  String profile_image, Role role) {
        this.socialType = socialType;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profile_image = profile_image;
        this.phone_number = phone_number;
        this.stamp_cnt = stamp_cnt;
        this.role = role;
    }




}
