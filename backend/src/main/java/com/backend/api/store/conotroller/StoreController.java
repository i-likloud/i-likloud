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
import com.backend.global.error.ErrorResponse;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import com.backend.global.util.CustomApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "상점 홈", description = "상점 홈에 들어가면 필요한 정보 반환을 수행합니다."+"\n\n### [ 수행절차 ]\n\n"+"- try it out 해주세요\n\n")
    @CustomApi
    @GetMapping("/home")
    public ResponseEntity<StoreResponseDto> getStore (@MemberInfo MemberInfoDto memberInfoDto){
        String email = memberInfoDto.getEmail();
        ProfileDto profileDto = mypageService.getMyProfile(email);

        // 상점 아이템리스트 + 보유여부
        List<StoreWithAccessoryDto> allAccessoriesWithOwnership = storeService.getAllAccessorysWithOwnership(profileDto.getMemberId());

        StoreResponseDto responseDto = StoreResponseDto.builder()
                .profile(profileDto)
                .allAccessoriesWithOwnership(allAccessoriesWithOwnership)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "악세사리 구매", description = "악세사리를 구매하는 메서드입니다."+"\n\n### [ 수행절차 ]\n\n"+"- 구매할 아이템의 store id 값을 넣어주세요\n\n"+"- try it out 해주세요\n\n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "#### 성공"),
            @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오",
                    content =@Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."),
                                    @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"),
                                    @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"),
                                    @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"),
                                    @ExampleObject( name = "400_User-004", value = "해당 회원은 존재하지 않습니다."),
                                    @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."),
                                    @ExampleObject( name = "404_Item-001", value = "아이템을 찾을 수 없습니다."),
                                    @ExampleObject( name = "404_Item-002", value = "이미 구매한 아이템입니다."),
                                    @ExampleObject( name = "500", value = "서버에러")}))})
    @PostMapping("/buy/{storeId}")
    public ResponseEntity<StoreBuyResponseDto> buyAccessory(@RequestParam Long storeId,@MemberInfo MemberInfoDto memberInfoDto) {
        String email = memberInfoDto.getEmail();
        ProfileDto profileDto = storeService.buyAccessory(email, storeId);

        // 보유 아이템 목록 응답
        List<AccessoryDto> myAccessories = mypageService.getMyAccessory(profileDto.getMemberId());

        StoreBuyResponseDto responseDto = StoreBuyResponseDto.builder()
                .profile(profileDto)
                .myAccessory(myAccessories)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "악세사리 등록", description = "악세사리를 등록합니다."+"\n\n### [ 수행절차 ]\n\n"+"- accessoryName, accessoryPrice 넣고 try it out 해주세요. (아래 예시값 그대로 try it out 해도 괜찮습니다.)\n\n"+"- try it out 해주세요\n\n")
    @CustomApi
    @PostMapping("/accessoryUpload")
    public ResponseEntity<Store> uploadAccessory(@RequestBody AccessoryUploadRequestDto requestDto) {
        Store storedAccessory = storeService.uploadAccessory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(storedAccessory);
    }
}
