package com.backend.api.photo.service;

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
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PhotoInfoService {

    private final PhotoRepository photoRepository;
    private final BookmarkRepository bookmarkRepository;
    private final DrawingRepository drawingRepository;
    private final LikesRepository likesRepository;

    // 최신순 전체 조회
    @Transactional(readOnly = true)
    @Cacheable(value = "allPhotos", key = "'searchAllDesc'")
    public List<PhotoInfoResponseDto> searchAllDesc() {

        long startTime = System.currentTimeMillis();

        List<Photo> photos = photoRepository.findAllByOrderByCreatedAtDesc();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Query Execution Time: " + executionTime + " milliseconds");

        return photos.stream()
                .map(PhotoInfoResponseDto::new)
                .collect(Collectors.toList());
    }

    // 인기순 전체 조회
    @Transactional(readOnly = true)
    public List<PhotoInfoResponseDto> searchAllPickCntDesc(){

        long startTime = System.currentTimeMillis();

        List<Photo> photos = photoRepository.findAllByOrderByPickCntDesc();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Query Execution Time: " + executionTime + " milliseconds");

        return photos.stream()
                .map(PhotoInfoResponseDto::new)
                .collect(Collectors.toList());

    }

    // 북마크 전체 조회
    @Transactional(readOnly = true)
    public List<PhotoInfoResponseDto> searchAllBookmarkCntDesc(){

        long startTime = System.currentTimeMillis();

        List<Photo> photos = photoRepository.findAllByOrderByBookmarkCntDesc();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Query Execution Time: " + executionTime + " milliseconds");

        return photos.stream()
                .map(PhotoInfoResponseDto::new)
                .collect(Collectors.toList());
    }

    // 상세 사진 조회
    @Transactional
    public PhotoDetailDto getPhotoDetail(Long photoId, Member member){
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PHOTO));

        boolean memberBookmarked = false;

        Bookmarks bookmark = this.getBookmarkIfExists(member, photo.getPhotoId());

        // 북마크 되어있으면 true
        if (bookmark != null){
            memberBookmarked = true;
        }

        return new PhotoDetailDto(photo, memberBookmarked);
    }

    // 특정 사진과 관련된 모든 그림
    @Transactional(readOnly = true)
    public List<DrawingWithBookmarksDto> getDrawingsByPhotoId(Long photoId, Member member) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 photo가 존재하지 않습니다."));

        List<Drawing> drawings = drawingRepository.findByPhotoOrderByLikesCountDesc(photo);

        List<Long> drawingIds = drawings.stream().map(Drawing::getDrawingId).collect(Collectors.toList());

        // member에 의해 좋아요된 drawingId 목록 가져오기
        Set<Long> likedDrawingIds = likesRepository.findLikedDrawingIdsByMember(member.getMemberId(), drawingIds);

        return drawings.stream()
                .map(drawing -> new DrawingWithBookmarksDto(drawing, likedDrawingIds.contains(drawing.getDrawingId())))
                .collect(Collectors.toList());
    }

    // 삭제
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allPhotos", allEntries = true),
            @CacheEvict(value = "photos", key = "#member.memberId")
    })
    public void delete(Long photoId, Member member) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(()-> new BusinessException(ErrorCode.NOT_FOUND_PHOTO));

        // 본인 사진 아닌 경우
        if(!photo.getMember().getMemberId().equals(member.getMemberId())){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_MEMBER);
        }

        // 북마크 일괄 삭제
        bookmarkRepository.deleteBookmarksByPhotoId(photoId);

        // Photo와 연관된 Drawing들의 photo 필드를 null로 설정하여 연관 관계 해제
        drawingRepository.unlinkDrawingsFromPhoto(photoId);

        // photo 삭제
        photoRepository.delete(photo);
    }

    // 사진 즐겨찾기
    @Transactional
    @CacheEvict(value = "bookMarks", key = "#member.memberId")
    public void pickPhoto(Long photoId, Member member) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 photo가 존재하지 않습니다."));

        Bookmarks bookmark = Bookmarks.builder()
                .member(member)
                .photo(photo)
                .build();

        bookmarkRepository.save(bookmark);

        // 즐겨찾기 수 증가
        photoRepository.increaseBookmarkCount(photoId);
    }

    // 사진 즐겨찾기 취소
    @Transactional
    @CacheEvict(value = "bookMarks", key = "#member.memberId")
    public void unpickPhoto(Long photoId, Member member, Bookmarks bookmark) {
        // 북마크 제거
        bookmarkRepository.delete(bookmark);
        // 즐겨찾기 수 감소
        photoRepository.decreaseBookmarkCount(photoId);

    }

    // 북마크확인, 있으면 북마크 반환
    public Bookmarks getBookmarkIfExists(Member member, Long photoId) {
        return bookmarkRepository.findByMemberMemberIdAndPhotoPhotoId(member.getMemberId(), photoId)
                .orElse(null);
    }

}
