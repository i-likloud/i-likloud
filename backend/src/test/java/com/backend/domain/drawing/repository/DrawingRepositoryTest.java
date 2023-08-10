package com.backend.domain.drawing.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.comment.entity.Comment;
import com.backend.domain.comment.repository.CommentRepository;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.nft.entity.Nft;
import com.backend.domain.nft.repository.NftRepository;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DrawingRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DrawingRepository drawingRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NftRepository nftRepository;

    @Autowired
    private EntityManager entityManager;


    //테스트 데이터
    Member member;
    Photo photo;

    // 데이터 초기화
    @BeforeEach
    public void setUp() {
        member = new Member(SocialType.KAKAO, "test@example.com", "TestUser", 0, 0, 0, 0, 0, Role.MEMBER);
        member = memberRepository.save(member);

        photo = new Photo(1L, member, "123", 0, null, null ,0);
        photo = photoRepository.save(photo);
    }


    @Test
    @Transactional
    void findAllWithMemberByCreatedAtDesc() {
        //given
        Drawing drawing1 = new Drawing(1L,"test1","test1","test","test",member,photo,null,0,0,false);
        Drawing drawing2 = new Drawing(2L,"test2","test2","test","test",member,photo,null,0,0,false);
        Drawing drawing3 = new Drawing(3L,"test3","test3","test","test",member,photo,null,0,0,false);

        Drawing saveDrawing1 = drawingRepository.save(drawing1);
        Drawing saveDrawing2 = drawingRepository.save(drawing2);
        Drawing saveDrawing3 = drawingRepository.save(drawing3);

        // when
        List<Drawing> drawingList = drawingRepository.findDrawingByMember_MemberId(member.getMemberId());
        System.out.println("리스트: " + drawingList);

        // then
//        assertTrue(drawingList.contains(saveDrawing1));
//        assertTrue(drawingList.contains(saveDrawing2));
//        assertTrue(drawingList.contains(saveDrawing3));
        assertEquals(3,drawingList.size());
        assertEquals(saveDrawing1,drawingList.get(0));
        assertEquals(saveDrawing2,drawingList.get(1));
        assertEquals(saveDrawing3,drawingList.get(2));
    }

    @Test
    @Transactional
    void findByPhotoOrderByLikesCountDesc() {
        //given
        Drawing drawing1 = new Drawing(1L,"test1","test1","test","test",member,photo,null,0,0,false);
        Drawing drawing2 = new Drawing(2L,"test2","test2","test","test",member,photo,null,0,0,false);
        Drawing drawing3 = new Drawing(3L,"test3","test3","test","test",member,photo,null,0,0,false);

        Drawing saveDrawing1 = drawingRepository.save(drawing1);
        Drawing saveDrawing2 = drawingRepository.save(drawing2);
        Drawing saveDrawing3 = drawingRepository.save(drawing3);

        // when
        List<Drawing> drawingList = drawingRepository.findAllWithMemberByCreatedAtDesc();
        System.out.println("리스트: " + drawingList);

        // then
        assertTrue(drawingList.contains(saveDrawing1));
        assertTrue(drawingList.contains(saveDrawing2));
        assertTrue(drawingList.contains(saveDrawing3));
        assertEquals(3,drawingList.size());
    }

    @Test
    @Transactional
    void unlinkDrawingsFromPhoto() {
        //given
        Drawing drawing1 = new Drawing(1L, "test1", "test1", "test", "test", member, photo, null, 0, 0, false);
        Drawing drawing2 = new Drawing(2L, "test2", "test2", "test", "test", member, photo, null, 0, 0, false);

        drawingRepository.save(drawing1);
        drawingRepository.save(drawing2);

        System.out.println("세이브: " + drawing1.getPhoto().getPhotoId()+ ',' + drawing2.getPhoto().getPhotoId());

        //when
        drawingRepository.unlinkDrawingsFromPhoto(photo.getPhotoId());

        // 변경 사항을 DB에 즉시 반영
          entityManager.flush();
        entityManager.clear();

        List<Drawing> drawings = drawingRepository.findAll();


        //then
        for (Drawing drawing : drawings) {
            assertNull(drawing.getPhoto());
        }
    }

    @Test
    void unlinkDrawingFromNft() {
        //given
        Drawing drawing1 = new Drawing(1L, "test1", "test1", "test", "test", member, photo, null, 0, 0, false);

        Drawing saveDrawing = drawingRepository.save(drawing1);

        Nft nft = new Nft(1L,"test","test","test","test","test","test",member,saveDrawing,null);

        Nft saveNft = nftRepository.save(nft);
        //when
        drawingRepository.unlinkDrawingFromNft(saveNft.getNftId());

        // 변경 사항을 DB에 즉시 반영
        entityManager.flush();
        entityManager.clear();

        List<Drawing> drawings = drawingRepository.findAll();
        System.out.println("엔에프티: " + drawings);
        System.out.println("엔에프티2: " + drawings.get(0).getNft());


        //then
        assertNull(drawings.get(0).getNft());
    }

    
    // 댓글 못 가져옴
    @Test
    @Transactional
    void findByDrawingIdWithComments() {
        // given
        Drawing drawing = new Drawing(1L,"test1","test1","test","test",member,photo,null,0,0,false);
        Drawing saveDrawing = drawingRepository.save(drawing);
        Comment comment = new Comment("string",saveDrawing,member,"test");
        Comment saveComment = commentRepository.save(comment);
        System.out.println("saveDrawing: " + saveDrawing);

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<Drawing> find = drawingRepository.findByDrawingIdWithComments(saveDrawing.getDrawingId());

        Drawing finds = find.get();

        System.out.println("결과: " + finds);
        System.out.println("댓글: " + finds.getComments());
        System.out.println("댓글2: " + saveComment);

        // then
        assertEquals(finds.getDrawingId(),saveDrawing.getDrawingId());

        boolean containsSaveComment = finds.getComments().stream()
                .anyMatch(commentInList  -> commentInList .getCommentId().equals(saveComment.getCommentId()));
        assertTrue(containsSaveComment);

        // 추가로, 댓글 내용도 확인할 수 있도록 아래와 같이 코드를 작성합니다.
        boolean containsSaveCommentContent = finds.getComments().stream()
                .anyMatch(commentInList  -> commentInList .getContent().equals(saveComment.getContent()));
        assertTrue(containsSaveCommentContent);

    }

    @Test
    @Transactional
    void increaseLikesCount() {

        Drawing drawing = new Drawing(1L, "test1", "test1", "test", "test", member, photo, null, 0, 0, false);
        Drawing saveDrawing = drawingRepository.save(drawing);

        drawingRepository.increaseLikesCount(saveDrawing.getDrawingId());

        entityManager.flush();
        entityManager.clear();

        Drawing updatedDrawing = drawingRepository.findById(saveDrawing.getDrawingId()).orElse(null);


        assertEquals(saveDrawing.getLikesCount() + 1, updatedDrawing.getLikesCount());
    }

    @Test
    void decreaseLikesCount() {
        Drawing drawing = new Drawing(1L, "test1", "test1", "test", "test", member, photo, null, 0, 5, false);
        Drawing saveDrawing = drawingRepository.save(drawing);

        drawingRepository.decreaseLikesCount(saveDrawing.getDrawingId());

        entityManager.flush();
        entityManager.clear();

        Drawing updatedDrawing = drawingRepository.findById(saveDrawing.getDrawingId()).orElse(null);


        assertEquals(saveDrawing.getLikesCount() - 1, updatedDrawing.getLikesCount());
    }
}