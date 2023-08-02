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
        Member member = memberService.findMemberById(memberId);
        List<Store> allaccessorys = storeRepository.findAll();

        return allaccessorys.stream()
                .map(store -> {
                    boolean owned = accessoryService.isAlreadyOwned(member, store.getStoreId());
                    return new StoreWithAccessoryDto(store, owned);
                })
                .collect(Collectors.toList());
    }

    public Store findStoreByStoreId(Long storeId){
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ACCESSORY));
    }

    @Transactional
    public ProfileDto buyAccessory(String email, Long storeId) {
        Member member = memberService.findMemberByEmail(email);
        Store buyStore = findStoreByStoreId(storeId);
//
//        if (AllAccessoriesWithOwnership)
        int targetCoin = buyStore.getAccessoryPrice();
        int currentGoldCoin = member.getGoldCoin();

        if (currentGoldCoin < targetCoin) {
            throw new RuntimeException("gold-coin이 부족합니다.");
        }

        List<Accessory> myAccessories = accessoryRepository.findByMember(member);
        boolean alreadyOwned = myAccessories.stream().anyMatch(accessory -> accessory.getAccessoryId().equals(storeId));

        if (!alreadyOwned) {
            member.setGoldCoin(currentGoldCoin - targetCoin);
            memberRepository.save(member);

//            // storeRepository를 사용하여 악세사리의 store 정보를 가져옴
//            Store store = storeRepository.findById(buyAccessory.getStoreId())
//                    .orElseThrow(() -> new RuntimeException("악세사리의 store 정보를 찾을 수 없습니다."));

            // member가 sotr_id번의 buyAccessory를 샀다.
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
