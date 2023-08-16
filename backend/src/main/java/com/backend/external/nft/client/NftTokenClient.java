package com.backend.external.nft.client;

import com.backend.external.nft.dto.DeleteTokenDto;
import com.backend.external.nft.dto.NftTokenDto;
import com.backend.external.nft.dto.NftTransferDto;
import com.backend.external.nft.dto.TokenListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "https://kip17-api.klaytnapi.com", name="createToken")
public interface NftTokenClient {

    // 토큰 발행
    @PostMapping(value = "/v2/contract/{contractAddress}/token", consumes = "application/json")
    NftTokenDto.Response createToken(
            @RequestHeader("x-chain-id") String chainId,
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String contractAddress,
            @RequestBody NftTokenDto.Request requestDto
    );

    // 모든 토큰 조회
    @GetMapping(value = "/v2/contract/{contractAlias}/token", consumes = "application/json")
    TokenListDto.Response getAllTokenList(@RequestHeader("x-chain-id") String chainId,
                                          @RequestHeader("Authorization") String authorizationHeader,
                                          @PathVariable String contractAlias);

    // 특정 사용자 지갑 조회
    @GetMapping(value = "/v2/contract/{contractAlias}/owner/{address}", consumes = "application/json")
    TokenListDto.Response getTokenList(@RequestHeader("x-chain-id") String chainId,
                                       @RequestHeader("Authorization") String authorizationHeader,
                                       @PathVariable String contractAlias,
                                       @PathVariable String address);

    // 토큰 소멸
    @DeleteMapping(value = "/v2/contract/{contractAddress}/token/{tokenId}", consumes = "application/json")
    DeleteTokenDto.Response deleteToken(
            @RequestHeader("x-chain-id") String chainId,
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String contractAddress,
            @PathVariable String tokenId,
            @RequestBody DeleteTokenDto.Request requestDto);


    // 토큰 전송
    @PostMapping(value = "/v2/contract/{contractAlias}/token/{tokenId}", consumes = "application/json")
    NftTransferDto.Response transferToken(@RequestHeader("x-chain-id") String chainId,
                                          @RequestHeader("Authorization") String authorizationHeader,
                                          @PathVariable String contractAlias,
                                          @PathVariable String tokenId,
                                          @RequestBody NftTransferDto.Request request);
}

