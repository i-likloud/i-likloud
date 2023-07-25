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

        member.updateAdditionalInfo(request.getNickname(), request.getProfileFace(), request.getProfileColor(), Role.MEMBER);

        return MemberDto.Response.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileColor(member.getProfileColor())
                .profileFace(member.getProfileFace())
                .role(member.getRole())
                .socialType(member.getSocialType())
                .build();

    }

}
