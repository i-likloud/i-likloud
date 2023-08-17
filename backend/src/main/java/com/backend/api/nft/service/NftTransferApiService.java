package com.backend.api.nft.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.backend.api.nft.dto.NftTransferResponseDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.nft.Dto.TokenMetaData;
import com.backend.domain.nft.entity.Nft;
import com.backend.domain.nft.entity.NftTransfer;
import com.backend.domain.nft.repository.NftTransferRepository;
import com.backend.domain.nft.service.NftService;
import com.backend.external.nft.client.NftTokenClient;
import com.backend.external.nft.dto.NftTransferDto;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class NftTransferApiService {

    private final NftService nftService;
    private final MemberService memberService;
    private final NftTokenClient nftTokenClient;
    private final NftTransferRepository nftTransferRepository;
    private final StringEncryptor stringEncryptor;
    private final AmazonS3Client amazonS3Client;
    private final NftApiService nftApiService;

    @Value("${KAS.client.authorization}")
    private String authorization;

    @Value("${KAS.client.contract-alias}")
    private String contract;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 토큰 전송 시작
    @Transactional
    @CacheEvict(value = "nft", key = "#fromMember.memberId")
    public NftTransferResponseDto transferToken(Member fromMember, Long nftId, Long toMemberId, String message) {
        // 보낼 토큰
        Nft nft = nftService.findNftById(nftId);
        // 토큰 받을 멤버
        Member toMember = memberService.findMemberById(toMemberId);
        // 받는 사람 지갑 없는 경우
        if (toMember.getWallet() == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_WALLET);
        }
        // 본인에게 선물 x
//        if (fromMember.getMemberId().equals(toMemberId)){
//            throw new BusinessException(ErrorCode.CANNOT_GIFT_TO_SELF);
//        }
        // 보내는 사람 주소
        String sender = fromMember.getWallet();

        NftTransferDto.Request requestDto = NftTransferDto.Request.builder()
                        .sender(sender)
                        .owner(sender)
                        .to(toMember.getWallet())
                        .build();
        // key 검증 후 맞으면 거래 진행
        if (checkKeyId(nft, fromMember)) {
            // 클레이튼API 호출
            NftTransferDto.Response response = nftTokenClient.transferToken("1001", authorization, contract, nft.getTokenId(), requestDto);
            // 거래내역 엔티티 생성
            NftTransfer nftTransfer = uploadNftTransfer(nft, fromMember, toMember, response.getTransactionHash(), message);
            return new NftTransferResponseDto(nftTransfer);
        } else {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_KEY);
        }
    }

    // 거래내역 등록
    @Transactional
    public NftTransfer uploadNftTransfer(Nft nft, Member fromMember, Member toMember, String transactionHash, String message){
        NftTransfer nftTransfer = NftTransfer.builder()
                .toMember(toMember.getMemberId())
                .fromMember(fromMember.getMemberId())
                .tokenId(nft.getTokenId())
                .transactionHash(transactionHash)
                .message(message)
                .nft(nft)
                .build();

        // 소유자 보류
        nft.setMember(null);

        nftTransferRepository.save(nftTransfer);
        // 메타데이터 업데이트
        updateMetadata(nft.getNftMetadata(), toMember);

        return nftTransfer;
    }

    // 전송자 검증, nft의 멤버의 keyId와 보내는 사람 keyId가 같은지 해독해서 확인
    public boolean checkKeyId(Nft nft, Member fromMember) {
        String decryptedNftKeyId = stringEncryptor.decrypt(nft.getMember().getKeyId());
        String decryptedFromMemberKeyId = stringEncryptor.decrypt(fromMember.getKeyId());

        return decryptedNftKeyId.equals(decryptedFromMemberKeyId);
    }

    // 메타데이터 수정
    @Transactional
    public void updateMetadata(String metadataUrl, Member toMember) {
        // URL에서 키 추출 (예: "nft-metadata/0x5ee11f9fea4d339b.json")
        String key = metadataUrl.substring(metadataUrl.indexOf("nft-metadata/"));

        // 1. 기존 메타데이터 가져오기
        S3Object s3object = amazonS3Client.getObject(bucket, key);
        try (S3ObjectInputStream inputStream = s3object.getObjectContent()) {
            String existingMetaDataJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            // 2. 오너 변경
            TokenMetaData existingMetaData = new ObjectMapper().readValue(existingMetaDataJson, TokenMetaData.class);
            existingMetaData.setOwner(toMember.getEmail());

            // 3. 수정된 메타데이터를 JSON으로 변환
            String updatedMetaDataJson = new ObjectMapper().writeValueAsString(existingMetaData);

            // 4. 수정된 메타데이터를 동일한 키로 S3에 저장
            nftApiService.uploadMetadataToS3(updatedMetaDataJson, key);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update metadata", e);
        }
    }
}
