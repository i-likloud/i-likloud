package com.backend.domain.member.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    //테스트 데이터
    Member member;

    // 데이터 초기화
    @BeforeEach
    public void setUp() {
        member = new Member(SocialType.KAKAO, "test@example.com", "TestUser", 0, 0, 0, 0, 0, Role.MEMBER);
        member = memberRepository.save(member);
    }

    @Test
    void findByEmail() {
        //given

        //when
        Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());
        Member find = optionalMember.get();

        System.out.println("멤버: " + member);
        System.out.println("찾은 멤버: " + find);
        //then
        assertEquals(member,find);
    }

    @Test
    void findByNickname() {
        //given

        //when
        Optional<Member> optionalMember = memberRepository.findByNickname(member.getNickname());
        Member find = optionalMember.get();

        System.out.println("멤버: " + member);
        System.out.println("찾은 멤버: " + find);
        //then
        assertEquals(member,find);
    }

    @Test
    void findByNicknameContaining() {
        //given
        Member member1 = new Member(SocialType.KAKAO, "test1@example.com", "TestUser1", 0, 0, 0, 0, 0, Role.MEMBER);
        Member savemember1 = memberRepository.save(member1);

        Member member2 = new Member(SocialType.KAKAO, "test2@example.com", "TestUser2", 0, 0, 0, 0, 0, Role.MEMBER);
        Member savemember2 = memberRepository.save(member2);

        //when
        Optional<List<Member>> memberList = memberRepository.findByNicknameContaining("TestUser");

        System.out.println("리스트: " + memberList);

        //then
        assertEquals(3,memberList.get().size());
    }

    @Test
    void findByRefreshToken() {
        //given

        //when
        Optional<Member> optionalMember = memberRepository.findByRefreshToken(member.getRefreshToken());
        Member find = optionalMember.get();

        System.out.println("멤버: " + member);
        System.out.println("찾은 멤버: " + find);
        //then
        assertEquals(member,find);
    }
}