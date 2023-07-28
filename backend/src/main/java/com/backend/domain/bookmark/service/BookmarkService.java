package com.backend.domain.bookmark.service;

import com.backend.api.photo.dto.PhotoWithBookmarkDto;
import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.bookmark.repository.BookmarkRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.photo.entity.Photo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final MemberService memberService;


    public BookmarkService(BookmarkRepository bookmarkRepository, MemberService memberService) {
        this.bookmarkRepository = bookmarkRepository;
        this.memberService = memberService;
    }

    public List<PhotoWithBookmarkDto> getBookmarkedPhotosForUser(Long memberId) {
        Member member = memberService.findMemberById(memberId); // memberId로부터 Member 객체 조회

        List<Bookmarks> bookmarks = bookmarkRepository.findByMember(member);

        // 북마크된 사진들을 리스트로 반환
        List<PhotoWithBookmarkDto> bookmarkedPhotoDtos = bookmarks.stream()
                .map(bookmark -> new PhotoWithBookmarkDto(bookmark.getPhoto().getPhotoUrl()))
                .collect(Collectors.toList());

        return bookmarkedPhotoDtos;
    }
}
