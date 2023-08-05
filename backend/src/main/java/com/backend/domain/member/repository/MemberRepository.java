package com.backend.domain.member.repository;

import com.backend.api.member.dto.MemberSearchDto;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> getLikesByMemberId(Long memberId);
    Optional<List<Member>> findByNicknameContaining(String nickname);
    Optional<Member> findByRefreshToken(String refreshToken);
    Optional<Member> findBySocialType(SocialType socialType);
    boolean existsByNickname(String nickname);
}
