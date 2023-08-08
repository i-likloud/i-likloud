package com.backend.domain.bookmark.repository;

import com.backend.api.common.AccessToken;
import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.photo.entity.Photo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Transactional(readOnly = true)
class BookmarkRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private MemberRepository memberRepository;

    private String token = AccessToken.getNewToken();



    // 테스트 데이터
    private Member member;


    private Photo photo;

    @BeforeEach
    public void setUp() {
        // 테스트 데이터 초기화
        member = new Member(SocialType.KAKAO, "test@example.com", "TestUser", 0, 0, 0, 0, 0, Role.MEMBER);
        member = memberRepository.save(member);

        photo = new Photo(1L, member, "123", 0, null, null ,0);

    }

    @AfterEach
    public void tearDown() {
        // 테스트 데이터 제거
        bookmarkRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @Transactional
    @Rollback
    void findByMemberMemberIdAndPhotoPhotoId() throws Exception{

        ResultActions resultActions = mvc.perform(post("/api/photo/{photoId}/bookmark", photo.getPhotoId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());


        //given
        Bookmarks bookmarks1 = new Bookmarks(1L,member,photo);

        //when
        Optional<Bookmarks> bookmarks = bookmarkRepository.findByMemberMemberIdAndPhotoPhotoId(1L,photo.getPhotoId());
    
        System.out.println("멤버 " + member.getMemberId());
        System.out.println("포토 " + photo.getPhotoId());
        System.out.println("북마크 " + bookmarks);

        assertTrue(bookmarks.isPresent()); // 검증: 북마크가 존재하는지 확인
//        assertEquals(bookmarks1, bookmarks.get()); // 검증: 조회한 북마크와 초기에 생성한 북마크가 일치하는지 확인
    }

//    @Test
//    void findByMember() {
//    }
//
//    @Test
//    void deleteBookmarksByPhotoId() {
//    }
}