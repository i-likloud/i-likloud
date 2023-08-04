package com.backend.api.photo.service;

import com.backend.api.drawing.dto.DrawingWithBookmarksDto;
import com.backend.api.likes.service.LikesService;
import com.backend.api.photo.dto.PhotoDetailDto;
import com.backend.api.photo.dto.PhotoInfoResponseDto;
import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.bookmark.repository.BookmarkRepository;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PhotoInfoService {

    private final PhotoRepository photoRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final DrawingRepository drawingRepository;
    private final LikesService likesService;

    // 최신순 전체 조회
    @Transactional(readOnly = true)
    @Cacheable(value = "allPhotos", key = "'searchAllDesc'")
    public List<PhotoInfoResponseDto> searchAllDesc(Member member) {
        List<Photo> photos = photoRepository.findAllByOrderByCreatedAtDesc();

        return photos.stream()
                .map(PhotoInfoResponseDto::new)
                .collect(Collectors.toList());
    }

    // 인기순 전체 조회
    @Transactional(readOnly = true)
    public List<PhotoInfoResponseDto> searchAllPickCntDesc(Member member){
        List<Photo> photos = photoRepository.findAllByOrderByPickCntDesc();

        return photos.stream()
                .map(PhotoInfoResponseDto::new)
                .collect(Collectors.toList());

    }

    // 북마크 전체 조회
    @Transactional(readOnly = true)
    public List<PhotoInfoResponseDto> searchAllBookmarkCntDesc(Member member){
        List<Photo> photos = photoRepository.findAllByOrderByBookmarkCntDesc();

        return photos.stream()
                .map(PhotoInfoResponseDto::new)
                .collect(Collectors.toList());
    }

    // 상세 사진 조회
    @Transactional
    public PhotoDetailDto getPhotoDetail(Long photoId, Member member){
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PHOTO));

        boolean memberBookmarked = this.isAlreadyBookmarked(member, photo.getPhotoId());

        return new PhotoDetailDto(photo, memberBookmarked);
    }

    // 특정 사진과 관련된 모든 그림
    @Transactional(readOnly = true)
    public List<DrawingWithBookmarksDto> getDrawingsByPhotoId(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 photo가 존재하지 않습니다."));

        List<Drawing> drawings = drawingRepository.findByPhotoOrderByLikesCountDesc(photo);

        return drawings.stream()
                .map(drawing ->{
                    boolean memberLiked = likesService.isAlreadyLiked(new Member(),drawing.getDrawingId());
                    return new DrawingWithBookmarksDto(drawing, memberLiked);})
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

        if(!photo.getMember().getMemberId().equals(member.getMemberId())){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_MEMBER);
        }

        // Photo와 연관된 Drawing들의 photo 필드를 null로 설정하여 연관 관계 해제
        List<Drawing> drawings = photo.getDrawings();
        for (Drawing drawing : drawings) {
            drawing.setPhoto(null);
        }

        photoRepository.delete(photo);
    }

    // 사진 즐겨찾기
    @Transactional
    @CacheEvict(value = "bookMarks", key = "#memberId")
    public void pickPhoto(Long photoId, Long memberId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 photo가 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 member가 존재하지 않습니다."));

        Bookmarks bookmark = Bookmarks.builder()
                .member(member)
                .photo(photo)
                .build();

        bookmarkRepository.save(bookmark);

        // 즐겨찾기 수 증가
        photo.setBookmarkCnt(photo.getBookmarkCnt() +1);

    }

    // 사진 즐겨찾기 취소
    @Transactional
    @CacheEvict(value = "bookMarks", key = "#memberId")
    public void unpickPhoto(Long photoId, Long memberId) {
        Bookmarks bookmark = bookmarkRepository.findByMemberMemberIdAndPhotoPhotoId(memberId, photoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 즐겨찾기가 존재하지 않습니다."));

        bookmarkRepository.delete(bookmark);

        // 즐겨찾기 수 감소
        Photo photo = bookmark.getPhoto();
        photo.setBookmarkCnt(photo.getBookmarkCnt() - 1);

    }

    // 북마크확인
    public boolean isAlreadyBookmarked(Member member, Long photoId) {
        return bookmarkRepository.existsByMemberMemberIdAndPhotoPhotoId(member.getMemberId(), photoId);
    }

}
