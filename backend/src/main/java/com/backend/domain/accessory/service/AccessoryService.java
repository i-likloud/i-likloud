package com.backend.domain.accessory.service;

import com.backend.domain.accessory.entity.Accessory;
import com.backend.domain.accessory.repository.AccessoryRepository;
import com.backend.domain.member.entity.Member;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessoryService {

    private final AccessoryRepository accessoryRepository;

    public Accessory findAccessoryByAccessoryId(Long accessoryId){
        return accessoryRepository.findById(accessoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ACCESSORY));
    }

    // 보유여부 확인
    public boolean isAlreadyOwned(Member member, Long storeId) {
        return accessoryRepository.existsByMemberMemberIdAndStoreStoreId(member.getMemberId(), storeId);
    }
}
