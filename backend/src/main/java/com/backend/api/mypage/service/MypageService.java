package com.backend.api.mypage.service;

import com.backend.api.drawing.dto.DrawingListDto;
import com.backend.api.drawing.service.DrawingViewService;
import com.backend.api.mypage.dto.MypageInfoDto;
import com.backend.api.mypage.dto.ProfileDto;
import com.backend.api.photo.dto.PhotoInfoResponseDto;
import com.backend.api.photo.dto.PhotoWithBookmarkDto;
import com.backend.api.photo.dto.PhotoWithDrawingsResponseDto;
import com.backend.domain.accessory.entity.Accessory;
import com.backend.domain.accessory.repository.AccessoryRepository;
import com.backend.domain.accessory.repository.AccessoryUploadRequestDto;
import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.bookmark.repository.BookmarkRepository;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.likes.entity.Likes;
import com.backend.domain.likes.repository.LikesRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import com.backend.domain.store.dto.StoreWithAccessoryDto;
import com.backend.domain.store.entity.Store;
import com.backend.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MypageService {

    private final MemberService memberService;
    private final LikesRepository likesRepository;
    private final DrawingRepository drawingRepository;
    private final BookmarkRepository bookmarkRepository;
    private final PhotoRepository photoRepository;
    private final AccessoryRepository accessoryRepository;
    private final StoreRepository storeRepository;


    @Transactional(readOnly = true)
    public MypageInfoDto getMyInfo(String email) {
        Member member = memberService.findMemberByEmail(email);
        return MypageInfoDto.of(member);
    }

    @Transactional(readOnly = true)
    public ProfileDto getMyProfile(String email) {
        Member member = memberService.findMemberByEmail(email);
        return ProfileDto.of(member);
    }

    @Transactional
    public MypageInfoDto editNickname(Member member, String nickname){
        member.editNickname(nickname);
        return MypageInfoDto.of(member);
    }

    @Transactional
    public ProfileDto editProfile(String email, ProfileDto.editRequest request){
        Member member = memberService.findMemberByEmail(email);
        member.editProfile(request.getProfileFace(), request.getProfileColor(), request.getProfileAccessory());
        return ProfileDto.of(member);

    }

    public List<DrawingListDto> likeDrawing(Long memberId){
        List<Likes> list = likesRepository.findAllByMemberMemberId(memberId);
        return list.stream()
                    .map(like -> {
                        boolean memberLiked = likesRepository.existsByMemberMemberIdAndDrawingDrawingId(memberId, like.getDrawing().getDrawingId());
                        return new DrawingListDto(like.getDrawing(), memberLiked);
                    })
                    .collect(Collectors.toList());
    }

    public List<DrawingListDto> getMyDrawing(Long memberId) {
        List<Drawing> list = drawingRepository.findDrawingByMember_MemberId(memberId);

        return list.stream()
                .map(drawing -> {
                    boolean memberLiked = likesRepository.existsByMemberMemberIdAndDrawingDrawingId(memberId, drawing.getDrawingId());
                    return new DrawingListDto(drawing, memberLiked);
                })
                .collect(Collectors.toList());
    }

    public List<PhotoWithBookmarkDto> bookmarkPhoto(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        List<Bookmarks> bookmarks = bookmarkRepository.findByMember(member);

        return bookmarks.stream()
                .map(bookmark -> new PhotoWithBookmarkDto(bookmark.getPhoto()))
                .collect(Collectors.toList());
    }

    public List<PhotoWithBookmarkDto> getMyPhoto(Long memberId) {
        List<Photo> list = photoRepository.findAllByMemberMemberId(memberId);

        return list.stream()
                .map(photo -> new PhotoWithBookmarkDto(photo))
                .collect(Collectors.toList());
    }


    public List<StoreWithAccessoryDto> getMyAccessory(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        List<Accessory> accessories = accessoryRepository.findByMember(member);

        return accessories.stream()
                .map(accessory -> new StoreWithAccessoryDto(accessory.getStore()))
                .collect(Collectors.toList());


    }

    public List<StoreWithAccessoryDto> getAllAccessoriesWithOwnership(Long memberId) {
        List<Accessory> allAccessories = accessoryRepository.findAll();
        List<StoreWithAccessoryDto> ownedAccessoryIds = getMyAccessory(memberId);

        return allAccessories.stream()
                .map(accessory -> {
                    boolean owned = ownedAccessoryIds.contains(accessory.getStore().getStoreId());
                    return new StoreWithAccessoryDto(accessory.getStore());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ProfileDto buyAccessory(String email, Long accessoryId) {
        Member member = memberService.findMemberByEmail(email);

        int currentGoldCoin = member.getGoldCoin();

        if (currentGoldCoin < 1) {
            throw new RuntimeException("goldcoin이 부족합니다.");
        }

        List<Accessory> myAccessories = accessoryRepository.findByMember(member);
        boolean alreadyOwned = myAccessories.stream().anyMatch(accessory -> accessory.getAccessoryId().equals(accessoryId));

        if (!alreadyOwned) {
            member.setGoldCoin(currentGoldCoin - 1);

            Accessory purchasedAccessory = accessoryRepository.findById(accessoryId)
                    .orElseThrow(() -> new RuntimeException("악세사리를 찾을 수 없습니다."));

            // storeRepository를 사용하여 악세사리의 store 정보를 가져옴
            Store store = storeRepository.findById(purchasedAccessory.getStore().getStoreId())
                    .orElseThrow(() -> new RuntimeException("악세사리의 store 정보를 찾을 수 없습니다."));


            purchasedAccessory.setStore(store);
            purchasedAccessory.setMember(member);
            accessoryRepository.save(purchasedAccessory);
        }

        return ProfileDto.of(member);
    }

    @Transactional
    public StoreWithAccessoryDto uploadAccessory(AccessoryUploadRequestDto requestDto) {
        Store store = Store.builder()
                .itemName(requestDto.getItemName())
                .itemPrice(requestDto.getItemPrice())
                .build();

        store = storeRepository.save(store);

        // Accessory 테이블에도 추가
        Accessory accessory = Accessory.builder()
                .store(store)
                .build();

        accessory = accessoryRepository.save(accessory);

        return new StoreWithAccessoryDto(store);
    }

    public List<StoreWithAccessoryDto> getAccessory(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        List<Accessory> accessories = accessoryRepository.findByMember(member);

        return accessories.stream()
                .map(accessory -> new StoreWithAccessoryDto(accessory.getStore()))
                .collect(Collectors.toList());
    }
}

