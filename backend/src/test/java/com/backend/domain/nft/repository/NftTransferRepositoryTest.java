package com.backend.domain.nft.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.nft.entity.Nft;
import com.backend.domain.nft.entity.NftTransfer;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NftTransferRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private NftRepository nftRepository;

    @Autowired
    private DrawingRepository drawingRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private NftTransferRepository nftTransferRepository;

    @Autowired
    private EntityManager entityManager;


    // 테스트 데이터
    Member member,target;
    Drawing drawing;
    Photo photo;
    Nft nft;

    @BeforeEach
    public void setUp() {
        member = new Member(SocialType.KAKAO, "test@example.com", "TestUser", 0, 0, 0, 0, 0, Role.MEMBER);
        member = memberRepository.save(member);

        target = new Member(SocialType.KAKAO, "test2@example.com", "TestUser2", 0, 0, 0, 0, 0, Role.MEMBER);
        target = memberRepository.save(target);

        photo = new Photo(1L, member, "123", 0, null, null ,0);
        photo = photoRepository.save(photo);

        drawing = new Drawing(1L, "test1", "test1", "test", "test", member, photo, null, 0, 0, false);
        drawing = drawingRepository.save(drawing);

        nft = new Nft(1L, "test", "test", "test", "test", "test", "test", member,drawing,null);
        nft = nftRepository.save(nft);

    }

    @Test
    void findByToMember() {
        //given
        NftTransfer nftTransfer = new NftTransfer(1L,target.getMemberId(),member.getMemberId(),"test","test","test",nft);
        NftTransfer saveNftTransfer = nftTransferRepository.save(nftTransfer);

        //when
        List<NftTransfer> nftTransferList = nftTransferRepository.findByToMember(target.getMemberId());

        //then
        assertEquals(1,nftTransferList.size());
        assertTrue(nftTransferList.contains(saveNftTransfer));
    }

    @Test
    void deleteById() {
        //given
        NftTransfer nftTransfer = new NftTransfer(1L,target.getMemberId(),member.getMemberId(),"test","test","test",nft);
        NftTransfer saveNftTransfer = nftTransferRepository.save(nftTransfer);

        //when
        nftTransferRepository.deleteById(saveNftTransfer.getTransferId());

        List<NftTransfer> nftTransferList = nftTransferRepository.findAll();

        //then
        assertFalse(nftTransferList.contains(saveNftTransfer));
    }
}