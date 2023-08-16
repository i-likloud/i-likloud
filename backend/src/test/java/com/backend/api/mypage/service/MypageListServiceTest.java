package com.backend.api.mypage.service;


import com.backend.api.common.BaseIntegrationTest;
import com.backend.api.drawing.dto.DrawingListDto;
import com.backend.api.photo.dto.PhotoInfoResponseDto;
import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.bookmark.repository.BookmarkRepository;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.likes.entity.Likes;
import com.backend.domain.likes.repository.LikesRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MypageListServiceTest extends BaseIntegrationTest {

    @Autowired
    MyPageListService myPageListService;

    @MockBean
    DrawingRepository drawingRepository;

    @MockBean
    LikesRepository likesRepository;

    @MockBean
    PhotoRepository photoRepository;

    @MockBean
    BookmarkRepository bookmarkRepository;


    Drawing drawing1 = null;
    Drawing drawing2 = null;
    Drawing drawing3 = null;

    Photo photo1 = null;
    Photo photo2 = null;
    Photo photo3 = null;

    @BeforeEach
    void setUp() {

        drawing1 = Drawing.builder()
                .title("title")
                .artist("yt")
                .member(new Member())
                .imageUrl("123")
                .content("content")
                .nftYn(false)
                .likesCount(45)
                .viewCount(0)
                .build();


        drawing2 = Drawing.builder()
                .title("title2")
                .artist("yt")
                .member(new Member())
                .imageUrl("456")
                .content("content2")
                .nftYn(true)
                .likesCount(200)
                .viewCount(53)
                .build();

        drawing3 = Drawing.builder()
                .title("title3")
                .artist("yt")
                .member(new Member())
                .imageUrl("789")
                .content("content3")
                .nftYn(true)
                .likesCount(200)
                .viewCount(53)
                .build();

        photo1 = Photo.builder()
                .photoUrl("abc")
                .bookmarkCnt(1)
                .pickCnt(1)
                .build();

        photo2 = Photo.builder()
                .photoUrl("def")
                .bookmarkCnt(3)
                .pickCnt(2)
                .build();

        photo3 = Photo.builder()
                .photoUrl("xyz")
                .bookmarkCnt(0)
                .pickCnt(0)
                .build();

    }

    @Test
    public void getMyDrawing_내그림_테스트() {

        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);

        drawing1.setMember(member);
        drawing3.setMember(member);

        // 내 그림 목록
        List<Drawing> drawings = Arrays.asList(drawing1, drawing3);

        // given
        when(drawingRepository.findDrawingByMember_MemberId(memberId))
                .thenReturn(drawings);

        // when
        List<DrawingListDto> myDrawings = myPageListService.getMyDrawing(memberId);

        // then
        assertThat(myDrawings.size()).isEqualTo(2);

        assertThat(myDrawings.get(0)).usingRecursiveComparison().isEqualTo(new DrawingListDto(drawing1));
        assertThat(myDrawings.get(1)).usingRecursiveComparison().isEqualTo(new DrawingListDto(drawing3));
    }

    @Test
    public void likeDrawing_좋아요한그림_테스트() {

        // 멤버 초기화
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);

        // 1번 3번 그림 좋아요
        Likes like1 = new Likes();
        Likes like2 = new Likes();
        like1.setMember(member);
        like1.setDrawing(drawing1);
        like2.setMember(member);
        like2.setDrawing(drawing3);
        List<Likes> likes = Arrays.asList(like1, like2);

        //given
        when(likesRepository.findAllByMemberMemberId(memberId))
                .thenReturn(likes);

        // when
        List<DrawingListDto> likeDrawings = myPageListService.likeDrawing(memberId);

        // then
        assertThat(likeDrawings.size()).isEqualTo(2);
        assertThat(likeDrawings.get(0)).usingRecursiveComparison().isEqualTo(new DrawingListDto(drawing1));
        assertThat(likeDrawings.get(1)).usingRecursiveComparison().isEqualTo(new DrawingListDto(drawing3));

    }

    @Test
    public void getMyPhoto_내사진_테스트() {

        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);

        photo1.setMember(member);
        photo2.setMember(member);

        // 내사진 리스트
        List<Photo> photos = Arrays.asList(photo1, photo2);

        // given
        when(photoRepository.findAllByMemberMemberId(memberId))
                .thenReturn(photos);

        // when
        List<PhotoInfoResponseDto> myPhotos = myPageListService.getMyPhoto(memberId);

        // then
        assertThat(myPhotos.size()).isEqualTo(2);
        assertThat(myPhotos.get(0)).usingRecursiveComparison().isEqualTo(new PhotoInfoResponseDto(photo1));
        assertThat(myPhotos.get(1)).usingRecursiveComparison().isEqualTo(new PhotoInfoResponseDto(photo2));

    }

    @Test
    public void bookmarkPhoto_북마크사진_테스트() {
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);

        Bookmarks bookmark1 = new Bookmarks();
        bookmark1.setMember(member);
        bookmark1.setPhoto(photo3);

        Bookmarks bookmark2 = new Bookmarks();
        bookmark2.setMember(member);
        bookmark2.setPhoto(photo2);

        photo2.setMember(member);
        photo3.setMember(member);

        List<Bookmarks> bookmarks = Arrays.asList(bookmark1, bookmark2);

        // given
        when(bookmarkRepository.findAllByMemberMemberId(memberId)).thenReturn(bookmarks);

        // when
        List<PhotoInfoResponseDto> bookmarkPhoto = myPageListService.bookmarkPhoto(memberId);

        // then
        assertThat(bookmarkPhoto.size()).isEqualTo(2);
        assertThat(bookmarkPhoto.get(0)).usingRecursiveComparison().isEqualTo(new PhotoInfoResponseDto(photo3));
    }

}
