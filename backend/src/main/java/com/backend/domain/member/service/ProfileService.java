package com.backend.domain.member.service;

import com.backend.domain.member.entity.Member;

public interface ProfileService {
    Member editNickname(String email, String nickname);
}
