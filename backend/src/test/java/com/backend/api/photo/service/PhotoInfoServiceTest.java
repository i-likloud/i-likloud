package com.backend.api.photo.service;


import com.backend.api.common.BaseIntegrationTest;
import com.backend.api.drawing.dto.DrawingWithBookmarksDto;
import com.backend.api.photo.dto.PhotoDetailDto;
import com.backend.api.photo.dto.PhotoInfoResponseDto;
import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.bookmark.repository.BookmarkRepository;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.likes.repository.LikesRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import com.backend.global.error.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PhotoInfoServiceTest extends BaseIntegrationTest {

    @Autowired
    PhotoInfoService photoInfoService;

    @MockBean
    PhotoRepository photoRepository;

    @MockBean
    BookmarkRepository bookmarkRepository;

    @MockBean
    DrawingRepository drawingRepository;

    @MockBean
    LikesRepository likesRepository;


    Photo photo1 = null;
    Photo photo3 = null;
    Member member = null;

    @BeforeEach
    void setUp() {

        Long memberId = 1L;
        member = new Member();
        member.setMemberId(memberId);

        photo1 = Photo.builder()
                .photoUrl("abc")
                .bookmarkCnt(1)
                .pickCnt(11)
                .member(member)
                .build();


        photo3 = Photo.builder()
                .photoUrl("xyz")
                .bookmarkCnt(10)
                .pickCnt(0)
                .member(member)
                .build();

    }

    @Test
    public void searchAllDesc_사진최신순_테스트() {

        Photo photo2 = Photo.builder()
                .photoUrl("def")
                .bookmarkCnt(3)
                .pickCnt(2)
                .build();

        photo2.setMember(member);

        // given
        when(photoRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(Arrays.asList(photo2, photo1));

        // when
        List<PhotoInfoResponseDto> photos = photoInfoService.searchAllDesc();

        // then
        assertThat(photos.size()).isEqualTo(2);
        assertThat(photos.get(0)).usingRecursiveComparison().isEqualTo(new PhotoInfoResponseDto(photo2));
        assertThat(photos.get(1)).usingRecursiveComparison().isEqualTo(new PhotoInfoResponseDto(photo1));
    }

    @Test
    public void searchAllPickCntDesc_사진인기순_테스트() {

        Photo photo2 = Photo.builder()
                .photoUrl("def")
                .bookmarkCnt(3)
                .pickCnt(2)
                .build();

        photo2.setMember(member);

        // given
        when(photoRepository.findAllByOrderByPickCntDesc())
                .thenReturn(Arrays.asList(photo1, photo2));

        // when
        List<PhotoInfoResponseDto> photos = photoInfoService.searchAllPickCntDesc();

        // then
        assertThat(photos.size()).isEqualTo(2);
        assertThat(photos.get(0)).usingRecursiveComparison().isEqualTo(new PhotoInfoResponseDto(photo1));
        assertThat(photos.get(1)).usingRecursiveComparison().isEqualTo(new PhotoInfoResponseDto(photo2));
    }

    @Test
    public void searchAllBookmarkCntDesc_사진북마크순_테스트() {

        // given
        when(photoRepository.findAllByOrderByBookmarkCntDesc())
                .thenReturn(Arrays.asList(photo3, photo1));

        // when
        List<PhotoInfoResponseDto> photos = photoInfoService.searchAllBookmarkCntDesc();

        // then
        assertThat(photos.size()).isEqualTo(2);
        assertThat(photos.get(0)).usingRecursiveComparison().isEqualTo(new PhotoInfoResponseDto(photo3));
        assertThat(photos.get(1)).usingRecursiveComparison().isEqualTo(new PhotoInfoResponseDto(photo1));
    }

    @Test
    public void getPhotoDetail_사진상세_테스트() {

        Long photoId = 1L;
        photo1.setPhotoId(photoId);

        Optional<Bookmarks> bookmark = Optional.of(new Bookmarks());
        bookmark.get().setMember(member);
        bookmark.get().setPhoto(photo1);

        // given
        when(photoRepository.findById(photoId)).thenReturn(Optional.ofNullable(photo1));
        when(bookmarkRepository.findByMemberMemberIdAndPhotoPhotoId(member.getMemberId(), photoId))
                .thenReturn(bookmark);  // 북마크 눌렀다고 가정

        // when
        PhotoDetailDto photoDetailDto = photoInfoService.getPhotoDetail(photoId, member);

        // then
        assertThat(photoDetailDto.isMemberBookmarked()).isTrue();
        assertThat(photoDetailDto.getPhotoUrl()).isEqualTo(photo1.getPhotoUrl());
        assertThat(photoDetailDto.getPickCount()).isEqualTo(photo1.getPickCnt());
        assertThat(photoDetailDto.getBookmarkCount()).isEqualTo(photo1.getBookmarkCnt());

    }

    @Test
    public void getDrawingsByPhotoId_사진관련그림_테스트() {

        Long photoId = 1L;
        photo1.setPhotoId(photoId);

        Drawing drawing1 = new Drawing();
        drawing1.setPhoto(photo1);
        drawing1.setDrawingId(10L);
        drawing1.setMember(member);

        Drawing drawing2 = new Drawing();
        drawing2.setPhoto(photo1);
        drawing2.setDrawingId(20L);
        drawing2.setMember(member);

        List<Drawing> drawings = Arrays.asList(drawing1, drawing2);
        List<Long> drawingIds = Arrays.asList(drawing1.getDrawingId(), drawing2.getDrawingId());
        Set<Long> likedDrawingIds = Collections.singleton(10L); // 첫번째 그림만 좋아요한 상태

        // given
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo1)); // 특정 사진
        when(drawingRepository.findByPhotoOrderByLikesCountDesc(photo1)) // 관련 그림
                .thenReturn(drawings);
        when(likesRepository.findLikedDrawingIdsByMember(member.getMemberId(), drawingIds)) // 좋아요한 그림
                .thenReturn(likedDrawingIds);

        // when
        List<DrawingWithBookmarksDto> result = photoInfoService.getDrawingsByPhotoId(photoId, member);

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).isMemberLiked()).isTrue();
        assertThat(result.get(1).isMemberLiked()).isFalse();

    }

    @Test
    @Transactional
    public void delete_사진삭제_테스트() {

        Long photoId = 1L;
        photo1.setPhotoId(photoId);

        // given
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo1));

        // when
        photoInfoService.delete(photoId, member);

        // then
        verify(bookmarkRepository).deleteBookmarksByPhotoId(photoId);
        verify(drawingRepository).unlinkDrawingsFromPhoto(photoId);
        verify(photoRepository).delete(photo1);
    }

    @Test
    public void delete_사진삭제_UNAUTHORIZED_테스트() {

        Member anotherMember = new Member();
        anotherMember.setMemberId(2L);

        Long photoId = 1L;
        photo1.setPhotoId(photoId);
        photo1.setMember(anotherMember);

        // given
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo1));

        // then
        assertThrows(BusinessException.class, () -> {
            //when
            photoInfoService.delete(photoId, member);
        });
    }

    @Test
    public void pickPhoto_사진즐겨찾기_테스트() {
        Long photoId = 1L;
        photo1.setPhotoId(photoId);
        photo1.setBookmarkCnt(10); // 북마크횟수 초기화

        Bookmarks bookmark = new Bookmarks();
        bookmark.setMember(member);
        bookmark.setPhoto(photo1);

        // given
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo1));
        doAnswer(invocation -> {
            photo1.setBookmarkCnt(photo1.getBookmarkCnt() + 1);
            return null;
        }).when(photoRepository).increaseBookmarkCount(photoId);

        // when
        photoInfoService.pickPhoto(photoId, member);

        // then
        assertThat(photo1.getBookmarkCnt()).isEqualTo(11);

    }

}
