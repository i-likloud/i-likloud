package com.backend.api.mypage.service;

import com.backend.api.drawing.dto.DrawingListDto;
import com.backend.api.mypage.dto.MypageInfoDto;
import com.backend.api.nft.dto.NftGiftDto;
import com.backend.api.nft.dto.NftListResponseDto;
import com.backend.api.photo.dto.PhotoInfoResponseDto;
import com.backend.domain.accessory.dto.AccessoryDto;
import com.backend.domain.accessory.entity.Accessory;
import com.backend.domain.accessory.repository.AccessoryRepository;
import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.bookmark.repository.BookmarkRepository;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.likes.entity.Likes;
import com.backend.domain.likes.repository.LikesRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.nft.entity.Nft;
import com.backend.domain.nft.entity.NftTransfer;
import com.backend.domain.nft.repository.NftRepository;
import com.backend.domain.nft.repository.NftTransferRepository;
import com.backend.domain.nft.service.NftService;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageListService {

    private final MemberService memberService;
    private final LikesRepository likesRepository;
    private final DrawingRepository drawingRepository;
    private final BookmarkRepository bookmarkRepository;
    private final PhotoRepository photoRepository;
    private final AccessoryRepository accessoryRepository;
    private final NftRepository nftRepository;
    private final NftTransferRepository nftTransferRepository;
    private final NftService nftService;

    @Cacheable(value = "likes", key = "#memberId") // 좋아요 그림 캐시 적용
    public List<DrawingListDto> likeDrawing(Long memberId){
        List<Likes> list = likesRepository.findAllByMemberMemberId(memberId);
        return list.stream()
                .map(like -> {return new DrawingListDto(like.getDrawing());
                })
                .collect(Collectors.toList());
    }

    @Cacheable(value = "drawings", key = "#memberId") // 내 그림 캐시 적용
    public List<DrawingListDto> getMyDrawing(Long memberId) {
        List<Drawing> list = drawingRepository.findDrawingByMember_MemberId(memberId);

        return list.stream()
                .map(DrawingListDto::new)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "bookMarks", key = "#memberId") // 북마크 사진 캐시 적용
    public List<PhotoInfoResponseDto> bookmarkPhoto(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        List<Bookmarks> bookmarks = bookmarkRepository.findByMember(member);

        return bookmarks.stream()
                .map(bookmark -> new PhotoInfoResponseDto(bookmark.getPhoto()))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "photos", key = "#memberId") // 내 사진 캐시 적용
    public List<PhotoInfoResponseDto> getMyPhoto(Long memberId) {
        List<Photo> list = photoRepository.findAllByMemberMemberId(memberId);

        return list.stream()
                .map(PhotoInfoResponseDto::new)
                .collect(Collectors.toList());
    }

    // 보유 아이템 조회
    public List<AccessoryDto> getMyAccessory(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        List<Accessory> accessories = accessoryRepository.findByMember(member);

        return accessories.stream()
                .map(AccessoryDto::new)
                .collect(Collectors.toList());

    }

    // 보유 NFT 조회
    @Cacheable(value = "nft", key = "#memberId") // 내 NFT 캐시 적용
    public List<NftListResponseDto> getMyNft(Long memberId){
        Member member = memberService.findMemberById(memberId);
        List<Nft> nftList = nftRepository.findAllByMemberMemberId(memberId);

        return nftList.stream()
                .map(NftListResponseDto::new)
                .collect(Collectors.toList());
    }

    // 선물함 조회
    public List<NftGiftDto> getMyGift(Long memberId) {
        List<NftTransfer> nftTransfers = nftTransferRepository.findByToMember(memberId);

        return nftTransfers.stream()
                .map(nftTransfer -> {
                    // 보낸 사람의 프로필 정보
                    Member fromMember = memberService.findMemberById(nftTransfer.getFromMember());
                    MypageInfoDto mypageInfoDto = MypageInfoDto.of(fromMember);

                    // NFT 정보
                    Nft nft = nftService.findNftById(nftTransfer.getNft().getNftId());

                    return new NftGiftDto(nftTransfer, mypageInfoDto, nft);
                })
                .collect(Collectors.toList());
    }
}
