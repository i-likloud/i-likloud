package com.backend.api.store.conotroller;

import com.backend.api.mypage.dto.ProfileDto;
import com.backend.api.mypage.service.MypageService;
import com.backend.api.store.dto.StoreResponseDto;
import com.backend.api.store.dto.StoreBuyResponseDto;
import com.backend.api.store.service.StoreService;
import com.backend.domain.accessory.dto.AccessoryDto;
import com.backend.domain.accessory.dto.AccessoryUploadRequestDto;
import com.backend.domain.store.dto.StoreWithAccessoryDto;
import com.backend.domain.store.entity.Store;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Store", description = "상점 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final MypageService mypageService;
    private final StoreService storeService;

    @Operation(summary = "상점", description = "상점 조회 메서드입니다.")
    @GetMapping("/home")
    public ResponseEntity<StoreResponseDto> getStore (@MemberInfo MemberInfoDto memberInfoDto){
        String email = memberInfoDto.getEmail();
        ProfileDto profileDto = mypageService.getMyProfile(email);

        // 상점 아이템리스트 + 보유여부
        List<StoreWithAccessoryDto> allAccessoriesWithOwnership = storeService.getAllItemsWithOwnership(profileDto.getMemberId());

        StoreResponseDto responseDto = StoreResponseDto.builder()
                .profile(profileDto)
                .allAccessoriesWithOwnership(allAccessoriesWithOwnership)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "악세사리 구매", description = "악세사리를 구매하는 메서드입니다.")
    @PostMapping("/buy/{accessoryId}")
    public ResponseEntity<StoreBuyResponseDto> buyAccessory(@RequestParam Long accessoryId,@MemberInfo MemberInfoDto memberInfoDto) {
        String email = memberInfoDto.getEmail();
        ProfileDto profileDto = storeService.buyAccessory(email, accessoryId);

        // 보유 아이템 목록 응답
        List<AccessoryDto> myAccessories = mypageService.getMyAccessory(profileDto.getMemberId());

        StoreBuyResponseDto responseDto = StoreBuyResponseDto.builder()
                .profile(profileDto)
                .myAccessory(myAccessories)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "악세사리 등록", description = "악세사리를 등록하는 메서드입니다. 가격과 이름을 입력하세요.")
    @PostMapping("/accessoryUpload")
    public ResponseEntity<Store> uploadAccessory(@RequestBody AccessoryUploadRequestDto requestDto) {
        Store storedAccessory = storeService.uploadAccessory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(storedAccessory);
    }
}
