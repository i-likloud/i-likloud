package com.backend.oauth2.nft.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://wallet-api.klaytnapi.com", name = "wallet")
public interface WalletClient {

    @PostMapping(value = "/v2/account", consumes = "application/json")
    String createWallet(
            @RequestHeader("x-chain-id") String chainId,
            @RequestHeader("Authorization") String authorizationHeader
                        );

}
