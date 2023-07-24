package com.backend.api.member.service;

import com.backend.api.member.dto.MemberDto;
import com.backend.api.member.dto.MemberInfoResponseDto;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberService memberService;

    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMemberInfo(String email) {
        Member member = memberService.findMemberByEmail(email);
        return MemberInfoResponseDto.of(member);
    }

    // 추가정보 업데이트
    @Transactional
    public MemberDto.Response updateAdditionalInfo(MemberDto.UpdateRequest request, String email) {
        Member member = memberService.findMemberByEmail(email);

        member.updateNickname(request.getNickname());
        member.updateProfileImage(request.getProfile_image());
        member.updatePhoneNumber(request.getPhone_number());
        member.updateRole(Role.MEMBER);

        return MemberDto.Response.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profile_image(member.getProfile_image())
                .phone_number(member.getPhone_number())
                .build();

    }

}
