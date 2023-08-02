package com.backend.external.nft.client;

import com.backend.external.nft.dto.NftTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kip17-api.klaytnapi.com", name="createToken")
public interface NftTokenClient {

    @PostMapping(value = "/v2/contract/{contractAddress}/token", consumes = "application/json")
    NftTokenDto.Response createToken(
            @RequestHeader("x-chain-id") String chainId,
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String contractAddress,
            @RequestBody NftTokenDto.Request requestDto
    );
}

