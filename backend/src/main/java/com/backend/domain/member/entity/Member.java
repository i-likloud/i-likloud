package com.backend.domain.member.entity;

import com.backend.domain.accessory.entity.Accessory;
import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.common.BaseEntity;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.likes.entity.Likes;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.nft.entity.Nft;
import com.backend.domain.photo.entity.Photo;
import com.backend.global.jwt.dto.JwtDto;
import com.backend.global.util.DateTimeUtils;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
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

    private int profileFace;

    private int profileColor;

    private int profileAccessory;

    private int goldCoin;

    private int silverCoin;

    // NFT 관련
    private String wallet;
    private String keyId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(length = 250)
    private String refreshToken;

    @Column(name = "firebase_token")
    private String firebaseToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime tokenExpirationTime;

    // 그림과 OneToMany 관계
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Drawing> drawings = new ArrayList<>();

    // 사진과 OneToMany 관계
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();

    // 좋아요와 OneToMany 관계
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

    // 악세사리와 OnetoMany 관계
    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Accessory> accessories = new ArrayList<>();

    // 북마크와 OneToMany 관계
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Bookmarks> bookmarks = new ArrayList<>();

    // NFT와 OneToMany 관계
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Nft> nfts = new ArrayList<>();


    //== 멤버 필드 업데이트 ==//
    public void editNickname(String nickname){
        this.nickname = nickname;
    }

    public void editProfile(int profileFace, int profileColor, int profileAccessory){
        this.profileColor = profileColor;
        this.profileFace = profileFace;
        this.profileAccessory = profileAccessory;
    }

    // 가져오기
    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public void updateRole(Role role){
        this.role = role;
    }

    public void updateRefreshToken(JwtDto jwtDto) {
        this.refreshToken = jwtDto.getRefreshToken();
        this.tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(jwtDto.getRefreshTokenExpirationPeriod());
    }

    public void updateAdditionalInfo(String nickname, int profileFace, int profileColor, int profileAccessory, Role role) {
        this.nickname = nickname;
        this.profileFace = profileFace;
        this.profileColor = profileColor;
        this.profileAccessory = profileAccessory;
        this.role = role;
    }

    public void expireRefreshToken(LocalDateTime now) {
        this.tokenExpirationTime = now;
    }

    @Builder
    public Member(SocialType socialType, String email, String nickname,  int goldCoin,
                  int profileColor, int profileFace, int profileAccessory, int silverCoin, Role role) {
        this.socialType = socialType;
        this.email = email;
        this.nickname = nickname;
        this.profileColor = profileColor;
        this.profileFace = profileFace;
        this.profileAccessory = profileAccessory;
        this.silverCoin = silverCoin;
        this.goldCoin = goldCoin;
        this.role = role;
    }

    public void incrementSilverCoin(int coins) {
        this.silverCoin += coins;
    }

    public void minusSilverCoin(int coins) {
        this.silverCoin -= coins;
    }

    public void incrementGoldCoin(int coins) {
        this.goldCoin += coins;
    }

}
