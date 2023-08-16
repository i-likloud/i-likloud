package com.backend.api.store.service;

import com.backend.api.mypage.dto.ProfileDto;
import com.backend.domain.accessory.dto.AccessoryUploadRequestDto;
import com.backend.domain.accessory.entity.Accessory;
import com.backend.domain.accessory.repository.AccessoryRepository;
import com.backend.domain.accessory.service.AccessoryService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.store.dto.StoreWithAccessoryDto;
import com.backend.domain.store.entity.Store;
import com.backend.domain.store.repository.StoreRepository;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final AccessoryRepository accessoryRepository;
    private final AccessoryService accessoryService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    // 상점 아이템 조회(보유 여부 추가)
    public List<StoreWithAccessoryDto> getAllAccessorysWithOwnership(Long memberId) {

        long startTime = System.currentTimeMillis();

        List<Store> stores = storeRepository.findAll();

        // 현재 멤버가 구매한 아이템id 목록
        Set<Long> boughtStoreIds = accessoryRepository.findBoughtStoreIdsByMember(memberId);

        List<StoreWithAccessoryDto> response = stores.stream()
                .map(store -> new StoreWithAccessoryDto(store, boughtStoreIds.contains(store.getStoreId())))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Query Execution Time: " + executionTime + " milliseconds");

        return response;

    }

    public Store findStoreByStoreId(Long storeId){
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ACCESSORY));
    }

    // 악세사리 구매
    @Transactional
    public ProfileDto buyAccessory(Member member, Long storeId) {
        Store buyStore = findStoreByStoreId(storeId);

        int targetCoin = buyStore.getAccessoryPrice();
        int currentGoldCoin = member.getGoldCoin();

        // 코인 부족
        if (currentGoldCoin < targetCoin) {
            throw new RuntimeException("gold-coin이 부족합니다.");
        }

        // 보유여부 확인
        List<Accessory> myAccessories = accessoryRepository.findByMember(member);
        boolean alreadyOwned = myAccessories.stream().anyMatch(accessory -> accessory.getStore().getStoreId().equals(storeId));

        if (!alreadyOwned) {
            member.updateGoldCoin(currentGoldCoin - targetCoin);
            memberRepository.save(member);

            // member가 storeId번의 Accessory를 샀다.
            Accessory accessory = Accessory.builder()
                    .store(buyStore)
                    .member(member)
                    .build();

            accessoryRepository.save(accessory);
        } else{
            throw new BusinessException(ErrorCode.ALREADY_PURCHASED_ITEM);
        }

        return ProfileDto.of(member);
    }

    // 상품 등록
    @Transactional
    public Store uploadAccessory(AccessoryUploadRequestDto requestDto) {
        Store store = Store.builder()
                .accessoryName(requestDto.getAccessoryName())
                .accessoryPrice(requestDto.getAccessoryPrice())
                .build();

        store = storeRepository.save(store);

        return store;
    }
}
