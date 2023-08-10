package com.backend.domain.drawing.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.entity.DrawingFile;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.photo.entity.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DrawingFileRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DrawingFileRepository drawingFileRepository;

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
        // 파일 생성
        DrawingFile drawingFile1 = new DrawingFile("test", "test");
        // 파일 저장
        DrawingFile drawingFile = drawingFileRepository.save(drawingFile1);
        System.out.println("드로잉 파일: " + drawingFile);

        Optional<DrawingFile> findDrawingFile = drawingFileRepository.findFileByFileName(drawingFile.getFileName());


        System.out.println("찾은 파일: " + findDrawingFile);

        assertEquals(drawingFile,findDrawingFile.get());

    }
}