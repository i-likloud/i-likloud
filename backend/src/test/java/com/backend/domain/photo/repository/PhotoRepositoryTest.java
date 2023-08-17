package com.backend.domain.photo.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.photo.entity.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PhotoRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private EntityManager entityManager;

    // 테스트 데이터
    Member member;

    @BeforeEach
    public void setUp(){
        member = new Member(SocialType.KAKAO, "test@example.com", "TestUser", 0, 0, 0, 0, 0, Role.MEMBER);
        member = memberRepository.save(member);
    }

    @Test
    void findAllByOrderByCreatedAtDesc() {
        // given
        Photo photo1 = new Photo(1L, member, "123", 0, null, null ,0);
        Photo photo2 = new Photo(2L, member, "123", 0, null, null ,0);
        Photo photo3 = new Photo(3L, member, "123", 0, null, null ,0);

        Photo savePhoto1 = photoRepository.save(photo1);
        Photo savePhoto2 = photoRepository.save(photo2);
        Photo savePhoto3 = photoRepository.save(photo3);

        // when
        List<Photo> photoList = photoRepository.findAllByOrderByCreatedAtDesc();
        System.out.println("리스트: " + photoList);

        // then
        assertEquals(3, photoList.size());
        assertEquals(savePhoto3, photoList.get(0));
        assertEquals(savePhoto2, photoList.get(1));
        assertEquals(savePhoto1, photoList.get(2));
    }

    @Test
    void findAllByOrderByPickCntDesc() {
        // given
        Photo photo1 = new Photo(1L, member, "123", 0, null, null ,0);
        Photo photo2 = new Photo(2L, member, "123", 1, null, null ,0);
        Photo photo3 = new Photo(3L, member, "123", 2, null, null ,0);

        Photo savePhoto1 = photoRepository.save(photo1);
        Photo savePhoto2 = photoRepository.save(photo2);
        Photo savePhoto3 = photoRepository.save(photo3);

        // when
        List<Photo> photoList = photoRepository.findAllByOrderByPickCntDesc();
        System.out.println("리스트: " + photoList);

        // then
        assertEquals(3, photoList.size());
        assertEquals(savePhoto3, photoList.get(0));
        assertEquals(savePhoto2, photoList.get(1));
        assertEquals(savePhoto1, photoList.get(2));
    }

    @Test
    void findAllByOrderByBookmarkCntDesc() {
        // given
        Photo photo1 = new Photo(1L, member, "123", 0, null, null ,0);
        Photo photo2 = new Photo(2L, member, "123", 0, null, null ,1);
        Photo photo3 = new Photo(3L, member, "123", 0, null, null ,2);

        Photo savePhoto1 = photoRepository.save(photo1);
        Photo savePhoto2 = photoRepository.save(photo2);
        Photo savePhoto3 = photoRepository.save(photo3);

        // when
        List<Photo> photoList = photoRepository.findAllByOrderByBookmarkCntDesc();
        System.out.println("리스트: " + photoList);

        // then
        assertEquals(3, photoList.size());
        assertEquals(savePhoto3, photoList.get(0));
        assertEquals(savePhoto2, photoList.get(1));
        assertEquals(savePhoto1, photoList.get(2));
    }

    @Test
    void findAllByMemberMemberId() {
        // given
        Photo photo1 = new Photo(1L, member, "123", 0, null, null ,0);
        Photo photo2 = new Photo(2L, member, "123", 0, null, null ,1);
        Photo photo3 = new Photo(3L, member, "123", 0, null, null ,2);

        Photo savePhoto1 = photoRepository.save(photo1);
        Photo savePhoto2 = photoRepository.save(photo2);
        Photo savePhoto3 = photoRepository.save(photo3);

        // when
        List<Photo> photoList = photoRepository.findAllByMemberMemberId(member.getMemberId());
        System.out.println("리스트: " + photoList);

        // then
        assertEquals(3, photoList.size());
        assertTrue(photoList.contains(savePhoto1));
        assertTrue(photoList.contains(savePhoto2));
        assertTrue(photoList.contains(savePhoto3));

    }

    @Test
    void increaseBookmarkCount() {
        //given
        Photo photo1 = new Photo(1L, member, "123", 0, null, null ,0);
        Photo savePhoto1 = photoRepository.save(photo1);

        //when
        photoRepository.increaseBookmarkCount(savePhoto1.getPhotoId());

        entityManager.flush();
        entityManager.clear();

        Optional<Photo> updatePhoto = photoRepository.findById(savePhoto1.getPhotoId());
        Photo photo = updatePhoto.get();

        //then
        assertEquals(savePhoto1.getBookmarkCnt()+1,photo.getBookmarkCnt());
    }

    @Test
    void decreaseBookmarkCount() {
        //given
        Photo photo1 = new Photo(1L, member, "123", 0, null, null ,5);
        Photo savePhoto1 = photoRepository.save(photo1);

        //when
        photoRepository.decreaseBookmarkCount(savePhoto1.getPhotoId());

        entityManager.flush();
        entityManager.clear();

        Optional<Photo> updatePhoto = photoRepository.findById(savePhoto1.getPhotoId());
        Photo photo = updatePhoto.get();

        //then
        assertEquals(savePhoto1.getBookmarkCnt()-1,photo.getBookmarkCnt());
    }
}