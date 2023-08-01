package com.backend.oauth2.nft.client;

import com.backend.oauth2.nft.dto.NftTokenBodyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kip17-api.klaytnapi.com")
public interface NftTokenClient {

    @PostMapping(value = "/v2/contract/unijoa/token", consumes = "application/json")
    String createToken(
            @RequestHeader("x-chain-id") String chainId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody NftTokenBodyDto requestDto
    );
}

