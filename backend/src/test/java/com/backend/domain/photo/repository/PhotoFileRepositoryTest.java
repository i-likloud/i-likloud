package com.backend.domain.photo.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.entity.PhotoFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PhotoFileRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PhotoFileRepository photoFileRepository;

    // 테스트 데이터
    private Member member;




    @BeforeEach
    public void setUp() {
        // 테스트 데이터 초기화
        member = new Member(SocialType.KAKAO, "test@example.com", "TestUser", 0, 0, 0, 0, 0, Role.MEMBER);
        member = memberRepository.save(member);
    }

    @Test
    void findFileByFileName() {
        // given
        PhotoFile photoFile = new PhotoFile(1L,"test","test",null);
        PhotoFile savePhotoFile = photoFileRepository.save(photoFile);

        // when
        Optional<PhotoFile> optionalPhotoFile = photoFileRepository.findFileByFileName(savePhotoFile.getFileName());
        PhotoFile find = optionalPhotoFile.get();

        System.out.println("savePhotoFile" + savePhotoFile);
        System.out.println("find" + find);

        // then
        assertEquals(savePhotoFile, find);

    }
}