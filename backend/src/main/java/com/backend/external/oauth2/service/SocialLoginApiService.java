package com.backend.external.oauth2.service;

import com.backend.external.oauth2.model.OauthAttributes;

public interface SocialLoginApiService {

    OauthAttributes getUserInfo(String accessToken);
}
