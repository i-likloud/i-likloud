package com.backend.domain.member.repository;

import com.backend.api.member.dto.MemberSearchDto;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.entity.Member;
import jnr.a64asm.Mem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    Optional<List<Member>> findByNicknameContaining(String nickname);
    Optional<Member> findByRefreshToken(String refreshToken);

}
