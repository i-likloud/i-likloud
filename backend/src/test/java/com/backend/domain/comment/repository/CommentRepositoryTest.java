package com.backend.domain.comment.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.comment.entity.Comment;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private DrawingRepository drawingRepository;

    @Autowired
    private PhotoRepository photoRepository;


    // 테스트 데이터
    private Member member;
    private Photo photo;
    private Drawing drawing;

    @BeforeEach
    public void setUp() {
        // 테스트 데이터 초기화
        member = new Member(SocialType.KAKAO, "test@example.com", "TestUser", 0, 0, 0, 0, 0, Role.MEMBER);
        member = memberRepository.save(member);

        photo = new Photo(1L, member, "123", 0, null, null ,0);
        photo = photoRepository.save(photo);

        drawing = new Drawing(1L,"test","test","test","test",member,photo,null,0,0,false);
        drawing = drawingRepository.save(drawing);

    }

    @Test
    @Transactional
    void deleteCommentByDrawingId() {
        //given
        // 코멘트 생성
        Comment comment1 = new Comment("string",drawing,member,"test");
        // 코멘트 저장
        Comment comment = commentRepository.save(comment1);

        System.out.println("코멘트 : " + comment);
        // 코멘트 Id로 내용 조회
        List<Comment> commentList1 = commentRepository.findAllById(Collections.singleton(comment.getCommentId()));
        System.out.println("리스트1 : " + commentList1);


        //when
        // 확인하고자 하는 삭제 리포지토리
        commentRepository.deleteCommentByDrawingId(drawing.getDrawingId());
        // 코멘트 Id로 내용 조회
        List<Comment> commentList = commentRepository.findAllById(Collections.singleton(comment.getCommentId()));
        System.out.println("리스트 : " + commentList);
        // 비었는가 확인
        assertTrue(commentList.isEmpty());
    }
}