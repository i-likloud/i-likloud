package com.backend.domain.member.service;


import com.backend.domain.member.dto.MypageInfoDto;
import com.backend.domain.member.dto.ProfileDto;
import com.backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MypageService {

    private final MemberService memberService;

    @Transactional(readOnly = true)
    public MypageInfoDto getMyInfo(String email) {
        Member member = memberService.findMemberByEmail(email);
        return MypageInfoDto.of(member);
    }

    @Transactional(readOnly = true)
    public ProfileDto getMyProfile(String email) {
        Member member = memberService.findMemberByEmail(email);
        return ProfileDto.of(member);
    }

    @Transactional
    public MypageInfoDto editNickname(String email, String nickname){
        Member member = memberService.findMemberByEmail(email);
        member.editNickname(nickname);
        return MypageInfoDto.of(member);
    }
}
