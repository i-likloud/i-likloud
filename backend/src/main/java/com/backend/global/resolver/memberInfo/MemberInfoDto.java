package com.backend.global.resolver.memberInfo;

import com.backend.domain.member.constant.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
// JWT에서 추출한 정보
public class MemberInfoDto {

    private String email;
    private Role role;
}
