package com.backend.domain.bookmark.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class BookmarkRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PhotoRepository photoRepository;



    // 테스트 데이터
    private Member member;


    private Photo photo;
    private Photo photo1;

    @BeforeEach
    public void setUp() {
        // 테스트 데이터 초기화
        member = new Member(SocialType.KAKAO, "test@example.com", "TestUser", 0, 0, 0, 0, 0, Role.MEMBER);
        member = memberRepository.save(member);

        photo = new Photo(1L, member, "123", 0, null, null ,0);
        photo = photoRepository.save(photo);

        photo1 = new Photo(2L, member, "123", 0, null, null ,0);
        photo1 = photoRepository.save(photo);

    }

    // 예외 발생하면 작동 안함
    @AfterEach
    public void tearDown() {
        // 테스트 데이터 제거
        bookmarkRepository.deleteAll();
        memberRepository.deleteAll();
        photoRepository.deleteAll();

        System.out.println("deleteAll");
    }

    @Test
    @Transactional
    void findByMemberMemberIdAndPhotoPhotoId() throws Exception{

        //given
        Bookmarks bookmarks1 = new Bookmarks(member,photo);

        Bookmarks savedBookMark = bookmarkRepository.save(bookmarks1);

        //when
        Optional<Bookmarks> bookmarks = bookmarkRepository.findByMemberMemberIdAndPhotoPhotoId(member.getMemberId(),photo.getPhotoId());
    
//        System.out.println("멤버 " + member.getMemberId());
//        System.out.println("포토 " + photo.getPhotoId());
//        System.out.println("북마크 " + savedBookMark);
//        System.out.println("북마크 조회 " + bookmarks);

        assertTrue(bookmarks.isPresent()); // 검증: 북마크가 존재하는지 확인
        assertEquals(bookmarks1, bookmarks.get()); // 검증: 조회한 북마크와 초기에 생성한 북마크가 일치하는지 확인
    }

    @Test
    @Transactional
    void findByMember() {

        //given
        Bookmarks bookmarks1 = new Bookmarks(member,photo);
        Bookmarks bookmarks2 = new Bookmarks(member,photo1);

        Bookmarks savedBookMark1 = bookmarkRepository.save(bookmarks1);
        Bookmarks savedBookMark2 = bookmarkRepository.save(bookmarks2);

//        System.out.println("첫 번째 : " + savedBookMark1);
//        System.out.println("두 번째 : " + savedBookMark2);

        //when
        List<Bookmarks> bookmarksList = bookmarkRepository.findByMember(member);
//        System.out.println("리스트 : " + bookmarksList);

        assertTrue(bookmarksList.contains(savedBookMark1));
        assertTrue(bookmarksList.contains(savedBookMark2));
        assertEquals(2,bookmarksList.size());

    }

    @Test
    @Transactional
    void deleteBookmarksByPhotoId() {
        //given
        Bookmarks bookmarks1 = new Bookmarks(member,photo);

        Bookmarks savedBookMark = bookmarkRepository.save(bookmarks1);


        //when
        bookmarkRepository.deleteBookmarksByPhotoId(photo.getPhotoId());

        List<Bookmarks> bookmarksList = bookmarkRepository.findByMember(member);

        System.out.println("리스트 : " + bookmarksList);

        assertTrue(bookmarksList.isEmpty());

    }
}