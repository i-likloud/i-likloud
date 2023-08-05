package com.backend.external.nft.client;

import com.backend.external.nft.dto.DeleteTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kip17-api.klaytnapi.com", name="deleteToken")
public interface DeleteTokenClient {

    @DeleteMapping(value = "/v2/contract/{contractAddress}/token/{tokenId}", consumes = "application/json")
    DeleteTokenDto.Response deleteToken(
            @RequestHeader("x-chain-id") String chainId,
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String contractAddress,
            @PathVariable String tokenId,
            @RequestBody DeleteTokenDto.Request requestDto);
}
