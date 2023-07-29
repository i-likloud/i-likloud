package com.backend.api.member.service;

import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
public class MemberCoinService {

    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;


    // 은코인 증가
    @Transactional
    public void plusSilverCoin(Long memberId, int coins) {
        Member member = entityManager.find(Member.class, memberId);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_EXISTS);
        }
        member.incrementSilverCoin(coins);
        // EntityManager를 사용하는 경우 변경을 감지하기 때문에 save를 명시적으로 호출할 필요 없음

    }

    // 은코인 감소
    @Transactional
    public void minusSilverCoin(Long memberId, int coins){
        Member member = entityManager.find(Member.class, memberId);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_EXISTS);
        }

        member.minusSilverCoin(coins);

    }

    // 현재 가진 은코인 가져오기
    public int getSilverCoin(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_EXISTS));

        return member.getSilverCoin();
    }
}
