package com.backend.api.nft.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "NFT" , description = "NFT 관련 api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/nft")
public class NftTokenController {

    // nft 발행

}
