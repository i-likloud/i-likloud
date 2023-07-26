package com.backend.domain.member.service;

import com.backend.domain.member.constant.ProfileColor;
import com.backend.domain.member.constant.ProfileFace;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService{

    private final MemberRepository memberRepository;

    @Override
    public Member editNickname(String email, String nickname) {

        validateDuplicateNickname(nickname);

        Optional<Member> optional = memberRepository.findByEmail(email);
        Member member = null;

        if (optional.isEmpty()) {
            member = new Member();
        } else {
            member = optional.get();
            member.setNickname(nickname);
            memberRepository.save(member);
        }

        return member;
    }

    // 닉네임 중복확인
    private void validateDuplicateNickname(String nickname) {

        if (memberRepository.existsByNickname(nickname)) {
            throw new RuntimeException("이미 존재하는 nickname입니다.");
        }
    }

    @Override
    public Member editProfile(String email, ProfileFace profileFace, ProfileColor profileColor) {

        Optional<Member> optional = memberRepository.findByEmail(email);
        Member member = null;

        if (optional.isEmpty()) {
            member = new Member();
        } else {
            member = optional.get();
            member.setProfileFace(profileFace);
            member.setProfileColor(profileColor);
            memberRepository.save(member);
        }

        return member;
    }

}
