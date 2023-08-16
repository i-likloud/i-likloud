package com.backend.api.login.validator;

import com.backend.domain.member.constant.SocialType;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class OauthValidator {

    public void validateSocialType(String socialType) {
        if (!SocialType.isSocialType(socialType)) {
            throw new BusinessException(ErrorCode.INVALID_SOCIAL_TYPE);
        }
    }
}
