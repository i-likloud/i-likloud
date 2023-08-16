package com.backend.domain.history.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.history.constant.HistoryType;
import com.backend.domain.history.entity.History;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HistoryRepository historyRepository;


    //테스트 데이터
    Member member;

    @BeforeEach
    public void setUp() {
        member = new Member(SocialType.KAKAO, "test@example.com", "TestUser", 0, 0, 0, 0, 0, Role.MEMBER);
        member = memberRepository.save(member);
    }

    @Test
    @Transactional
    void findAllByMemberMemberIdOrderByCreatedAtDesc() {
        // given
        History history1 = new History(member, "test1", 1L, 1L, HistoryType.COMMENT);
        History history2 = new History(member, "test2", 1L, 1L, HistoryType.COMMENT);
        History history3 = new History(member, "test3", 1L, 1L, HistoryType.COMMENT);

        History saveHistory1 = historyRepository.save(history1);
        History saveHistory2 = historyRepository.save(history2);
        History saveHistory3 = historyRepository.save(history3);

        // when
        List<History> historyList = historyRepository.findAllByMemberMemberIdOrderByCreatedAtDesc(member.getMemberId());
        System.out.println("리스트: " + historyList);

        assertEquals(3, historyList.size());
        assertEquals(saveHistory1, historyList.get(2));
        assertEquals(saveHistory2, historyList.get(1));
        assertEquals(saveHistory3, historyList.get(0));
    }
}