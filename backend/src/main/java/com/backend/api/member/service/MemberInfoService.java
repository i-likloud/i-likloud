package com.backend.api.member.service;

import com.backend.api.member.dto.MemberDto;
import com.backend.api.member.dto.MemberFindDto;
import com.backend.api.member.dto.MemberInfoResponseDto;
import com.backend.api.member.dto.MemberSearchDto;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberService memberService;

    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMemberInfo(String email) {
        Member member = memberService.findMemberByEmail(email);
        return MemberInfoResponseDto.of(member);
    }

    @Transactional(readOnly = true)
    public MemberSearchDto getMember(Member memberId) {
        return MemberSearchDto.of(memberId);
    }

    @Transactional(readOnly = true)
    public List<MemberFindDto> getMemberList(List<Member> memberList) {
        List<MemberFindDto> result = new ArrayList<>();

        for (Member member : memberList) {
            MemberFindDto searchDto = MemberFindDto.of(member);
            result.add(searchDto);
        }

        return result;
    }


    // 추가정보 업데이트
    @Transactional
    public MemberDto.Response updateAdditionalInfo(MemberDto.UpdateRequest request, String email) {
        Member member = memberService.findMemberByEmail(email);

        member.updateAdditionalInfo(request.getNickname(), request.getProfileFace(), request.getProfileColor(),
                request.getProfileAccessory(),Role.MEMBER);

        return MemberDto.Response.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileColor(member.getProfileColor())
                .profileFace(member.getProfileFace())
                .profileAccessory(member.getProfileAccessory())
                .role(member.getRole())
                .socialType(member.getSocialType())
                .build();
    }
}
