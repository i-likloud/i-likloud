package com.backend.domain.nft.service;

import com.backend.domain.nft.Dto.KlaytnApiProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KasWalletService {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String accessKeyId;
    private final String accessKeySecret;
    private final String authorizationHeader;

    public KasWalletService(RestTemplate restTemplate, KlaytnApiProperties apiProperties) {
        this.restTemplate = restTemplate;
        this.baseUrl = apiProperties.getBaseUrl();
        this.accessKeyId = apiProperties.getAccessKeyId();
        this.accessKeySecret = apiProperties.getAccessKeySecret();
        this.authorizationHeader = apiProperties.getAuthorizationHeader();
    }

    public String createWallet() {
        String apiUrl = baseUrl + "/v2/account";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(accessKeyId, accessKeySecret);
        headers.set("x-chain-id", "1001");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return response.getBody();
    }
}