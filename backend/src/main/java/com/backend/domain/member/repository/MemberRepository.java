package com.backend.domain.member.repository;

import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> getLikesByMemberId(Long memberId);
    Optional<Member> findByRefreshToken(String refreshToken);
    Optional<Member> findBySocialType(SocialType socialType);
    boolean existsByNickname(String nickname);


}
