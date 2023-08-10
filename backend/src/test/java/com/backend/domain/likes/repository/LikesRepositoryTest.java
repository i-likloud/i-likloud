package com.backend.domain.likes.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.likes.entity.Likes;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class LikesRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DrawingRepository drawingRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private LikesRepository likesRepository;

    //테스트 데이터
    Member member;
    Photo photo;

    Drawing drawing;
    Drawing drawing1;
    Drawing drawing2;


    // 데이터 초기화
    @BeforeEach
    public void setUp() {
        member = new Member(SocialType.KAKAO, "test@example.com", "TestUser", 0, 0, 0, 0, 0, Role.MEMBER);
        member = memberRepository.save(member);

        photo = new Photo(1L, member, "123", 0, null, null ,0);
        photo = photoRepository.save(photo);

        drawing = new Drawing(1L,"test","test","test","test",member,photo,null,0,0,false);
        drawing = drawingRepository.save(drawing);

        drawing1 = new Drawing(2L,"test","test","test","test",member,photo,null,0,0,false);
        drawing1 = drawingRepository.save(drawing1);

        drawing2 = new Drawing(3L,"test","test","test","test",member,photo,null,0,0,false);
        drawing2 = drawingRepository.save(drawing2);
    }

    @Test
    void existsByMemberMemberIdAndDrawingDrawingId() {

        // given
        Likes likes = new Likes(1L,member,drawing);

        Likes saveLikes = likesRepository.save(likes);

        // when
        boolean likesYn = likesRepository.existsByMemberMemberIdAndDrawingDrawingId(member.getMemberId(), drawing.getDrawingId());

        assertTrue(likesYn);

    }

    @Test
    void findAllByMemberMemberId() {
        // given
        Likes likes1 = new Likes(1L,member,drawing);
        Likes likes2 = new Likes(2L,member,drawing);
        Likes likes3 = new Likes(3L,member,drawing);
        Likes saveLikes1 = likesRepository.save(likes1);
        Likes saveLikes2 = likesRepository.save(likes2);
        Likes saveLikes3 = likesRepository.save(likes3);

        // when
        List<Likes> likesList = likesRepository.findAllByMemberMemberId(member.getMemberId());
        System.out.println("리스트: " + likesList);

        // then
        assertEquals(3,likesList.size());
        assertTrue(likesList.contains(saveLikes1));
        assertTrue(likesList.contains(saveLikes2));
        assertTrue(likesList.contains(saveLikes3));

    }

    @Test
    void findLikedDrawingIdsByMember() {
        // given
        Likes likes1 = new Likes(1L,member,drawing);
        Likes likes2 = new Likes(2L,member,drawing1);
        Likes likes3 = new Likes(3L,member,drawing2);
        Likes saveLikes1 = likesRepository.save(likes1);
        Likes saveLikes2 = likesRepository.save(likes2);
        Likes saveLikes3 = likesRepository.save(likes3);

        List<Drawing> drawings = drawingRepository.findByPhotoOrderByLikesCountDesc(photo);
        System.out.println("목록: " + drawings);

        List<Long> drawingIds = drawings.stream().map(Drawing::getDrawingId).collect(Collectors.toList());
        System.out.println("리스트: " + drawingIds);

        // when
        Set<Long> likes = likesRepository.findLikedDrawingIdsByMember(member.getMemberId(),drawingIds);
        System.out.println("when: " + likes);

        // then
        assertEquals(3,likes.size());
        assertTrue(likes.contains(saveLikes1.getDrawing().getDrawingId()));
        assertTrue(likes.contains(saveLikes2.getDrawing().getDrawingId()));
        assertTrue(likes.contains(saveLikes3.getDrawing().getDrawingId()));

    }

    @Test
    void deleteLikesByDrawingId() {
        Likes likes1 = new Likes(1L,member,drawing);
        Likes saveLikes1 = likesRepository.save(likes1);

        likesRepository.deleteLikesByDrawingId(drawing.getDrawingId());

        List<Likes> likesList = likesRepository.findAllByMemberMemberId(member.getMemberId());

        System.out.println("삭제 리스트: " + likesList);

        assertTrue(likesList.isEmpty());


    }

    @Test
    void deleteByMemberMemberIdAndDrawingDrawingId() {
        Likes likes1 = new Likes(1L,member,drawing);
        Likes saveLikes1 = likesRepository.save(likes1);

        likesRepository.deleteByMemberMemberIdAndDrawingDrawingId(member.getMemberId(),drawing.getDrawingId());

        List<Likes> likesList = likesRepository.findAllByMemberMemberId(member.getMemberId());

        System.out.println("삭제 리스트: " + likesList);

        assertTrue(likesList.isEmpty());

    }
}