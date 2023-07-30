package com.backend.api.store.conotroller;

import com.backend.api.mypage.dto.ProfileDto;
import com.backend.api.mypage.service.MypageService;
import com.backend.api.store.dto.StoreResponseDto;
import com.backend.domain.accessory.entity.Accessory;
import com.backend.domain.accessory.repository.AccessoryUploadRequestDto;
import com.backend.domain.store.dto.StoreWithAccessoryDto;
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

    @Operation(summary = "상점", description = "상점 조회 메서드입니다.")
    @GetMapping("/home")
    public ResponseEntity<StoreResponseDto> getStore (@MemberInfo MemberInfoDto memberInfoDto){
        String email = memberInfoDto.getEmail();
        ProfileDto profileDto = mypageService.getMyProfile(email);
        List<StoreWithAccessoryDto> myAccessory = mypageService.getMyAccessory(profileDto.getMemberId());
        List<StoreWithAccessoryDto> allAccessoriesWithOwnership = mypageService.getAllAccessoriesWithOwnership(profileDto.getMemberId());

        StoreResponseDto responseDto = StoreResponseDto.builder()
                .profile(profileDto)
                .myAccessory(myAccessory)
                .allAccessoriesWithOwnership(allAccessoriesWithOwnership)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "악세사리 구매", description = "goldcoin 1개로 악세사리를 구매하는 메서드입니다.")
    @PostMapping("/buy")
    public ResponseEntity<StoreResponseDto> buyAccessory(@RequestParam Long accessoryId,@MemberInfo MemberInfoDto memberInfoDto) {
        String email = memberInfoDto.getEmail();
        ProfileDto profileDto = mypageService.buyAccessory(email, accessoryId);

        List<StoreWithAccessoryDto> myAccessories = mypageService.getMyAccessory(profileDto.getMemberId());
        List<StoreWithAccessoryDto> allAccessoriesWithOwnership = mypageService.getAllAccessoriesWithOwnership(profileDto.getMemberId());

        StoreResponseDto responseDto = StoreResponseDto.builder()
                .goldCoin(profileDto.getGoldCoin())
                .profile(profileDto)
                .myAccessory(myAccessories)
                .allAccessoriesWithOwnership(allAccessoriesWithOwnership)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "악세사리 등록", description = "악세사리를 등록하는 메서드입니다.")
    @PostMapping("/accessoryupload")
    public ResponseEntity<StoreWithAccessoryDto> uploadAccessory(@RequestBody AccessoryUploadRequestDto requestDto) {
        StoreWithAccessoryDto storedAccessory = mypageService.uploadAccessory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(storedAccessory);
    }
}
