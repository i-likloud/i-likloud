package com.backend.api.bookmark.controller;

import com.backend.api.photo.dto.PhotoWithBookmarkDto;
import com.backend.domain.bookmark.service.BookmarkService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.photo.entity.Photo;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Bookmark", description = "즐겨찾기 관련 api")
@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final MemberService memberService;


    public BookmarkController(BookmarkService bookmarkService, MemberService memberService){
        this.bookmarkService = bookmarkService;
        this.memberService = memberService;
    }

    @Operation(summary = "로그인 유저의 모든 즐겨찾기", description = "로그인한 유저가 즐겨찾기한 모든 사진을 보여주는 메소드입니다.")
    @GetMapping("/my-bookmarked-photos")
    public ResponseEntity<List<PhotoWithBookmarkDto>> getBookmarkedPhotosForCurrentUser(@MemberInfo MemberInfoDto memberInfoDto) {
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();
        List<PhotoWithBookmarkDto> bookmarkedPhotos = bookmarkService.getBookmarkedPhotosForUser(memberId);
        return ResponseEntity.ok(bookmarkedPhotos);
    }
}
