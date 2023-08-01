package com.backend.oauth2.nft.client;

import com.backend.oauth2.nft.dto.NftTokenBodyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kip17-api.klaytnapi.com", name="createToken")
public interface NftTokenClient {

//
//    @Value("${KAS.client.accessKeyId}")
//    private String accessKeyId;
//
//    @Value("${KAS.client.secretAccessKey}")
//    private String secretAccessKey;
//
//    @Value("${KAS.client.authorization}")
//    private String authorization;
//

    // 지갑 발급
//    private final WalletClient walletClient;


    @PostMapping(value = "/v2/contract/unijoa/token", consumes = "application/json")
    String createToken(
            @RequestHeader("x-chain-id") String chainId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody NftTokenBodyDto requestDto
    );
}

