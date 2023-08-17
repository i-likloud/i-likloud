package com.backend.domain.nft.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.nft.entity.Nft;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NftRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private NftRepository nftRepository;

    @Autowired
    private DrawingRepository drawingRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private EntityManager entityManager;

    // 테스트 데이터
    Member member;
    Drawing drawing;
    Photo photo;

    @BeforeEach
    public void setUp() {
        member = new Member(SocialType.KAKAO, "test@example.com", "TestUser", 0, 0, 0, 0, 0, Role.MEMBER);
        member = memberRepository.save(member);

        photo = new Photo(1L, member, "123", 0, null, null ,0);
        photo = photoRepository.save(photo);

        drawing = new Drawing(1L, "test1", "test1", "test", "test", member, photo, null, 0, 0, false);
        drawing = drawingRepository.save(drawing);

    }


    @Test
    void findNftByDrawingDrawingId() {
        //given
        Nft nft = new Nft(1L, "test", "test", "test", "test", "test", "test", member,drawing,null);
        Nft saveNft = nftRepository.save(nft);

        //when
        Optional<Nft> nftOptional = nftRepository.findNftByDrawingDrawingId(drawing.getDrawingId());
        Nft find = nftOptional.get();

        //then
        assertEquals(saveNft,find);
    }

    @Test
    void findAllByMemberMemberId() {

        //given

        Drawing drawing1 = new Drawing(1L, "test1", "test1", "test", "test", member, photo, null, 0, 0, false);
        Drawing drawing2 = new Drawing(2L, "test2", "test2", "test", "test", member, photo, null, 0, 0, false);

        Drawing saveDrawing1 = drawingRepository.save(drawing1);
        Drawing saveDrawing2 = drawingRepository.save(drawing2);

        Nft nft1 = new Nft(1L, "test", "test", "test", "test", "test", "test", member,drawing,null);
        Nft saveNft1 = nftRepository.save(nft1);

        Nft nft2 = new Nft(2L, "test", "test", "test", "test", "test", "test", member,saveDrawing1,null);
        Nft saveNft2 = nftRepository.save(nft2);

        Nft nft3 = new Nft(3L, "test", "test", "test", "test", "test", "test", member,saveDrawing2,null);
        Nft saveNft3 = nftRepository.save(nft3);

        System.out.println("회원아이디: " + member.getMemberId());

        //when
        List<Nft> nftList = nftRepository.findAllByMemberMemberId(member.getMemberId());

        System.out.println("리스트: " + nftList);

        //then
        assertEquals(3,nftList.size());
        assertTrue(nftList.contains(saveNft1));
        assertTrue(nftList.contains(saveNft2));
        assertTrue(nftList.contains(saveNft3));

    }

    @Test
    void findNftByNftId() {
        //given
        Nft nft = new Nft(1L, "test", "test", "test", "test", "test", "test", member,drawing,null);
        Nft saveNft = nftRepository.save(nft);

        //when
        Optional<Nft> nftOptional = nftRepository.findNftByNftId(saveNft.getNftId());
        Nft find = nftOptional.get();

        //then
        assertEquals(saveNft,find);
    }

    @Test
    void unlinkNftFromDrawing() {
        // Given
        Drawing drawing1 = new Drawing(1L, "test1", "test1", "test", "test", member, photo, null, 0, 0, false);
        Drawing saveDrawing1 = drawingRepository.save(drawing1);

        Nft nft = new Nft(1L, "test", "test", "test", "test", "test", "test", member, saveDrawing1, null);
        Nft saveNft = nftRepository.save(nft);

        saveDrawing1.setNft(saveNft);

        System.out.println("아이디: " + saveDrawing1.getDrawingId());

        // When
        nftRepository.unlinkNftFromDrawing(saveDrawing1.getDrawingId());

        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Nft> nftOptional = nftRepository.findNftByNftId(saveNft.getNftId());
        Nft find = nftOptional.get();


        assertNull(find.getDrawing());
    }
}