package com.backend.domain.nft.controller;

import com.backend.domain.nft.service.KasWalletService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private final KasWalletService kasWalletService;

    public WalletController(KasWalletService kasWalletService) {
        this.kasWalletService = kasWalletService;
    }

    @GetMapping("/create")
    @Operation(summary = "지갑생성", description = "테스트용")
    public String createWallet() {
        return kasWalletService.createWallet();
    }
}