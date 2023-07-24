package com.backend.oauth2.service;

import com.backend.oauth2.model.OauthAttributes;

public interface SocialLoginApiService {

    OauthAttributes getUserInfo(String accessToken);
}
