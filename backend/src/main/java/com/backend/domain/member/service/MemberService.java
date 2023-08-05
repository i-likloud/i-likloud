package com.backend.domain.member.service;

import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.likes.entity.Likes;
import com.backend.domain.likes.repository.LikesRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.AuthenticationException;
import com.backend.global.error.exception.BusinessException;
import jnr.a64asm.Mem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final DrawingRepository drawingRepository;

    public Member registerMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    public Member updateMember(Member updatedMember) {
        Long memberId = updatedMember.getMemberId();
        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));

        // 업데이트할 필드가 있다면 각 필드를 새로운 값으로 업데이트
        if (updatedMember.getFirebaseToken() != null) {
            existingMember.setFirebaseToken(updatedMember.getFirebaseToken());
        }

        // 회원 정보 업데이트
        return memberRepository.save(existingMember);
    }

    public String getAuthorFirebaseToken(Long drawingId) {
        Drawing drawing = drawingRepository.findById(drawingId)
                .orElseThrow(() -> new EntityNotFoundException("Drawing not found with id: " + drawingId));

        Member author = drawing.getMember(); // Drawing 엔티티에 작성자(Member) 정보가 있는 가정하에 작성자를 가져옴
        return author.getFirebaseToken();
    }

    // 중복 검증
    private void validateDuplicateMember(Member member) throws BusinessException {
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember.isPresent()){
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED_MEMBER);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Member findMemberByRefreshToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        LocalDateTime tokenExpirationTime = member.getTokenExpirationTime();
        if(tokenExpirationTime.isBefore(LocalDateTime.now())) {
            throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        return member;
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_EXISTS));
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));
    }

    public Member findMemberByDrawingId(Long drawingId){
        return memberRepository.findByDrawings_DrawingId(drawingId);
    }

    public List<Member> findMemberByNickname(String nickname) {
        return memberRepository.findByNicknameContaining(nickname)
                .orElseThrow(() ->  new BusinessException(ErrorCode.MEMBER_NOT_EXISTS));
    }
    public List<Member> findList() {
        return memberRepository.findAll();
    }

    public List<Likes> getLikesByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memberId: " + memberId));
        return member.getLikes();
    }

    public List<Likes> getDrawingsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memberId: " + memberId));
        return member.getLikes();
    }

    public List<Likes> getBookmarkByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memberId: " + memberId));
        return member.getLikes();
    }

    public List<Likes> getPhotosByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memberId: " + memberId));
        return member.getLikes();
    }
}
