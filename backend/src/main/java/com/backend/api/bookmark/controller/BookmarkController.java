package com.backend.api.bookmark.controller;

import com.backend.api.photo.dto.PhotoWithBookmarkDto;
import com.backend.domain.bookmark.service.BookmarkService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.photo.entity.Photo;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final MemberService memberService;


    public BookmarkController(BookmarkService bookmarkService, MemberService memberService){
        this.bookmarkService = bookmarkService;
        this.memberService = memberService;
    }

    @GetMapping("/my-bookmarked-photos")
    public ResponseEntity<List<PhotoWithBookmarkDto>> getBookmarkedPhotosForCurrentUser(@MemberInfo MemberInfoDto memberInfoDto) {
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();
        List<PhotoWithBookmarkDto> bookmarkedPhotos = bookmarkService.getBookmarkedPhotosForUser(memberId);
        return ResponseEntity.ok(bookmarkedPhotos);
    }
}
