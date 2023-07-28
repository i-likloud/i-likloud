package com.backend.api.member.service;

import com.backend.api.member.dto.MemberDto;
import com.backend.api.member.dto.MemberInfoResponseDto;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.member.service.MemberService;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMemberInfo(String email) {
        Member member = memberService.findMemberByEmail(email);
        return MemberInfoResponseDto.of(member);
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

    // 은코인 1 증가
    public void plusSilverCoin(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 member가 존재하지 않습니다."));

        member.incrementSilverCoin();

        // 멤버 엔티티 업데이트 저장
        memberRepository.save(member);
    }

    // 은코인 감소
    public void minusSilverCoin(Long memberId, int coinsToDeduct){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 member가 존재하지 않습니다."));

        member.minusSilverCoin();

        memberRepository.save(member);
    }
    // 현재 가진 은코인 가져오기
    public int getSilverCoin(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_EXISTS));

        return member.getSilverCoin();
    }

}
