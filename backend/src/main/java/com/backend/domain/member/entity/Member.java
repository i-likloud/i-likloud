package com.backend.domain.member.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.member.constant.ProfileColor;
import com.backend.domain.member.constant.ProfileFace;
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
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 30)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private ProfileFace profileFace;

    @Enumerated(EnumType.STRING)
    private ProfileColor profileColor;

    private String wallet;

    private int coinCount;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(length = 250)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime tokenExpirationTime;


    // 그림과 OneToMany 관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Drawing> drawings = new ArrayList<>();

    //== 멤버 필드 업데이트 ==//
    public void updateNickname(String updateNickname) {
        this.nickname = updateNickname;
    }

    public void updateProfileCOLOR(ProfileColor profileColor){
        this.profileColor = profileColor;
    }

    public void updateProfileFace(ProfileFace profileFace){
        this.profileFace = profileFace;
    }


    public void updateCoinCount(int coinCount){
        this.coinCount = coinCount;
    }

    public void updateRole(Role role){
        this.role = role;
    }

    public void updateRefreshToken(JwtDto jwtDto) {
        this.refreshToken = jwtDto.getRefreshToken();
        this.tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(jwtDto.getRefreshTokenExpirationPeriod());
    }

    public void updateAdditionalInfo(String nickname, ProfileFace profileFace, ProfileColor profileColor, Role role) {
        this.nickname = nickname;
        this.profileFace = profileFace;
        this.profileColor = profileColor;
        this.role = role;
    }

    public void expireRefreshToken(LocalDateTime now) {
        this.tokenExpirationTime = now;
    }

    @Builder
    public Member(SocialType socialType, String email, String nickname,  int coinCount,
                  ProfileColor profileColor, ProfileFace profileFace,Role role) {
        this.socialType = socialType;
        this.email = email;
        this.nickname = nickname;
        this.profileColor = profileColor;
        this.profileFace = profileFace;
        this.coinCount = coinCount;
        this.role = role;
    }




}
