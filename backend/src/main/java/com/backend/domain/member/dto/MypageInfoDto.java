package com.backend.domain.member.dto;

import com.backend.api.member.dto.MemberInfoResponseDto;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.member.constant.ProfileColor;
import com.backend.domain.member.constant.ProfileFace;
import com.backend.domain.member.entity.Member;
import com.backend.domain.photo.entity.Photo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MypageInfoDto {
    private long memberId;
    private String nickname;
    private ProfileFace profileFace;
    private ProfileColor profileColor;
//    private Drawing drawing;
//    private Photo photo;

//    private final DrawingRepository drawingRepository;
//    private final PhotoRepository photoRepository;

//    public MyPageInfoDto(DrawingRepository drawingRepository, PhotoRepository photoRepository) {
//        this.drawingRepository = drawingRepository;
//        this.photoRepository = photoRepository;
//    }

//    public static MyPageInfoDto of(Member member, DrawingRepository drawingRepository, PhotoRepository photoRepository) {
//        MyPageInfoDto myPageInfoDto = new MyPageInfoDto(drawingRepository, photoRepository);
//        myPageInfoDto.setMemberId(member.getMemberId());
//        myPageInfoDto.setNickname(member.getNickname());
//        myPageInfoDto.setProfileFace(member.getProfileFace());
//        myPageInfoDto.setProfileColor(member.getProfileColor());
//
//        // 해당 회원이 like한 Drawing 조회
//        List<Drawing> likedDrawings = myPageInfoDto.getDrawingRepository().findLikedDrawingsByMember(member);
//        myPageInfoDto.setDrawing(likedDrawings);
//
//        // 해당 회원이 bookmark한 Photo 조회
//        List<Photo> bookmarkedPhotos = myPageInfoDto.getPhotoRepository().findBookmarkedPhotosByMember(member);
//        myPageInfoDto.setPhoto(bookmarkedPhotos);
//
//        return myPageInfoDto;
//    }
    public static MypageInfoDto of (Member member) {
        return MypageInfoDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileFace(member.getProfileFace())
                .profileColor(member.getProfileColor())
                .build();
    }
}
