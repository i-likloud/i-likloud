package com.backend.api.photo.service;

import com.backend.api.drawing.dto.DrawingWithLikesDto;
import com.backend.api.photo.dto.PhotoInfoResponseDto;
import com.backend.api.photo.dto.PhotoWithDrawingsResponseDto;
import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.bookmark.repository.BookmarkRepository;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PhotoInfoService {

    private final PhotoRepository photoRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final DrawingRepository drawingRepository;

    // 최신순 전체 조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllDesc() {
        List<Photo> photos = photoRepository.findAllByOrderByCreatedAtDesc();

        List<PhotoInfoResponseDto> photoInfoResponseDtos = photos.stream()
                .map(PhotoInfoResponseDto::new)
                .collect(Collectors.toList());


        Principal principal;



        return ResponseEntity.ok(photoInfoResponseDtos);
    }

    // 인기순 전체 조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllPickCntDesc(){
        List<Photo> photos = photoRepository.findAllByOrderByPickCntDesc();

        List<PhotoInfoResponseDto> photoInfoResponseDtos = photos.stream()
                .map(PhotoInfoResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(photoInfoResponseDtos);

    }

    // 북마크 전체 조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<PhotoInfoResponseDto>> searchAllBookmarkCntDesc(){
        List<Photo> photos = photoRepository.findAllByOrderByBookmarkCntDesc();

        List<PhotoInfoResponseDto> photoInfoResponseDtos = photos.stream()
                .map(PhotoInfoResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(photoInfoResponseDtos);
    }

    // 특정 사진과 관련된 모든 그림
    @Transactional(readOnly = true)
    public List<DrawingWithLikesDto> getDrawingsByPhotoId(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 photo가 존재하지 않습니다."));

        List<Drawing> drawings = drawingRepository.findByPhotoOrderByLikesCountDesc(photo);

        return drawings.stream()
                .map(drawing -> new DrawingWithLikesDto(drawing))
                .collect(Collectors.toList());
    }

    // 삭제
    public void delete(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

            photoRepository.delete(photo);
    }

    // 사진 즐겨찾기
    public ResponseEntity<String> pickPhoto(Long photoId, Long memberId) {
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
        photoRepository.save(photo);

        return ResponseEntity.ok("사진을 즐겨찾기했습니다.");
    }

    // 사진 즐겨찾기 취소
    public ResponseEntity<String> unpickPhoto(Long photoId, Long memberId) {
        Bookmarks bookmark = bookmarkRepository.findByMemberMemberIdAndPhotoPhotoId(memberId, photoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 즐겨찾기가 존재하지 않습니다."));

        bookmarkRepository.delete(bookmark);

        // 즐겨찾기 수 감소
        Photo photo = bookmark.getPhoto();
        photo.setBookmarkCnt(photo.getBookmarkCnt() - 1);
        photoRepository.save(photo);

        return ResponseEntity.ok("사진 즐겨찾기를 취소했습니다.");
    }

    // 사진으로 그림그리면 pickCnt 1씩 올라가는 기능
    public void handlePhotoClick(Long photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new EntityNotFoundException("사진을 찾을 수 없습니다"));

        // pickCnt를 증가시킴
        photo.incrementPickCnt();

        // 사진 엔티티 업데이트 저장
        photoRepository.save(photo);
    }
}
