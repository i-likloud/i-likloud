package com.ssafy.likloud.data.repository

import com.ssafy.likloud.data.api.NetworkResult
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.DrawingDetailDto
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.data.model.MemberInfoDto
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.data.model.NftGiftDto
import com.ssafy.likloud.data.model.NftListDto
import com.ssafy.likloud.data.model.NftRegistDto
import com.ssafy.likloud.data.model.NftWalletDto
import com.ssafy.likloud.data.model.photo.PhotoUploadResponseDto
import com.ssafy.likloud.data.model.request.LoginRequest
import com.ssafy.likloud.data.model.response.LoginResponse
import com.ssafy.likloud.data.model.response.ReLoginResponse
import com.ssafy.likloud.data.model.SampleDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.model.drawing.DrawingUploadResponse
import com.ssafy.likloud.data.model.request.LoginAdditionalRequest
import com.ssafy.likloud.data.model.request.MemberInfoRequest
import com.ssafy.likloud.data.model.request.ProfileEditRequest
import com.ssafy.likloud.data.model.response.AccessoryResponse
import com.ssafy.likloud.data.model.response.MemberInfoResponse
import com.ssafy.likloud.data.model.response.StoreItemBuyResponse
import com.ssafy.likloud.data.model.response.StoreResponse
import retrofit2.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BaseRepository {

    // 등록 요청
    suspend fun getComment(
        id: Int
    ): NetworkResult<SampleDto>

    suspend fun getUser(
        id: Int
    ): NetworkResult<UserDto>

    // 토큰 리프래쉬
    suspend fun postRefreshToken(
    ): NetworkResult<ReLoginResponse>

    /**
     * 회원가입 / 로그인에 사용합니다.
     */
    suspend fun postLogin(
        loginRequest: LoginRequest
    ): NetworkResult<LoginResponse>


    /**
     * 로그아웃 합니다.
     */
    suspend fun postLogout(
    ): Response<String>

    /**
     * 현재 로그인 되어있는 토큰을 가지고 멤버정보를 가져옵니다.
     */
    suspend fun getMemberInfo(
        memberInfoRequest: MemberInfoRequest
    ): NetworkResult<MemberInfoResponse>

    /**
     * 회원가입 후 newToken을 헤더에 담아 가져옵니다. MEMBER로 올라가며 가져온 토큰으로 api호출이 가능합니다.
     */
    suspend fun patchAdditionalInfo(
        loginAdditionalRequest: LoginAdditionalRequest
    ): retrofit2.Response<ReLoginResponse>

    /**
     * 그림 게시물 조회
     * orderBy에 "?orderBy=likesCount"(좋아요 순), "?orderBy=viewCount"(조회수 순)
     */
    suspend fun getDrawingList(
        orderBy: String
    ): NetworkResult<MutableList<DrawingListDto>>

    suspend fun getDrawingDetail(
        drawingId: Int
    ): NetworkResult<DrawingDetailDto>

    /**
     * 사진을 업로드 하여 구름인지 아닌지 결과값을 가져옵니다.
     */
    suspend fun postPhotoMultipart(
        multipartBodyPartList: List<MultipartBody.Part>,
        memberInfoDto: MemberInfoDto
    ): Response<List<PhotoUploadResponseDto>>


    suspend fun postSinglePhotoMultipart(
        multipartBodyPart: MultipartBody.Part,
        memberInfoDto: MemberInfoDto
    ): NetworkResult<List<PhotoUploadResponseDto>>


    suspend fun postDrawingMultipart(
        file: MultipartBody.Part,
        photoId: Int,
        title: RequestBody,
        content: RequestBody,
        memberInfoDto: MemberInfoDto
    ): NetworkResult<DrawingUploadResponse>

    /**
     * 사진 게시물 조회
     * parameter로 new(최신순)-기본 / pick(그린 수 순) / bookmarkdesc(즐찾순)
     */
    suspend fun getPhotoList(
        orderBy: String
    ): NetworkResult<MutableList<PhotoListDto>>

    /**
     * 특정 멤버 프로필 조회
     */
    suspend fun getMemberProfile(
        memberId: Int
    ): NetworkResult<MemberProfileDto>

    /**
     * 해당 사진에 대한 그림 목록 조회
     */
    suspend fun getPhotoDrawingList(
        photoId: Int
    ): NetworkResult<MutableList<DrawingListDto>>

    /**
     * 내 악세서리 목록 조회
     */
    suspend fun getMyAccessoryList(
        memberInfoRequest: MemberInfoRequest
    ): NetworkResult<MutableList<AccessoryResponse>>

    /**
     * 프로필 수정
     */
    suspend fun editMyProfile(
        profileEditRequest: ProfileEditRequest
    ): NetworkResult<MemberInfoResponse>

    /**
     * 개임 성공 시 은코인 상승
     */
    suspend fun plusSilver(): NetworkResult<String>

    /**
     * 내가 그린 그림 조회(마이페이지)
     */
    suspend fun getMyDrawingListDtoList(): NetworkResult<MutableList<DrawingListDto>>

    /**
     * 내가 좋아요 한 그림 조회(마이페이지)
     */
    suspend fun getLikeDrawingListDtoList(): NetworkResult<MutableList<DrawingListDto>>

    /**
     * 내가 올린 사진 조회(마이페이지)
     */
    suspend fun getMyPhotoListDtoList(): NetworkResult<MutableList<PhotoListDto>>

    /**
     * 상점 페이지 들어갈 때 조회
     */
    suspend fun getStoreInfo(
        memberInfoRequest: MemberInfoRequest
    ): NetworkResult<StoreResponse>

    /**
     * 악세서리 구매
     */
    suspend fun postBuyAccessory(
        storeId: Int,
        memberInfoRequest: MemberInfoRequest
    ): NetworkResult<StoreItemBuyResponse>

    /**
     * 내가 즐찾한 사진 조회(마이페이지)
     */
    suspend fun getBookmarkPhotoListDtoList(): NetworkResult<MutableList<PhotoListDto>>

    /**
     * 사진 상세 조회
     */
    suspend fun getCurrentPhotoListDto(photoId: Int): NetworkResult<PhotoListDto>

    /**
     * 좋아요 조회
     */
    suspend fun changeDrawingLike(drawingId: Int): NetworkResult<String>

    /**
     * 사진 즐찾
     */
    suspend fun changePhotoBookmark(photoId: Int): NetworkResult<String>
    /**
     * 댓글 등록
     */
    suspend fun registDrawingComment(drawingId: Int, content: String): NetworkResult<CommentDto>
    /**
     * 댓글 삭제
     */
    suspend fun deleteDrawingComment(commentId: Int): NetworkResult<String>
    /**
     * 내 NFT 조회
     */
    suspend fun getMyNftList(): NetworkResult<MutableList<NftListDto>>
    /**
     * NFT 발급
     */
    suspend fun registNft(drawingId: Int): NetworkResult<NftRegistDto>
    /**
     * 내 선물함 조회
     */
    suspend fun getNftGiftList(): NetworkResult<MutableList<NftGiftDto>>
    /**
     * 닉네임으로 유저 검색
     */
    suspend fun getCurrentSearchUserList(@Path("nickname") nickname: String): NetworkResult<MutableList<MemberInfoResponse>>
    /**
     * nft 선물하기
     */
    suspend fun sendGift(@Path("nftId") nftId: Int, @Path("toMemberId") toMemberId: Int, @Query("message") message: String): NetworkResult<NftGiftDto>
    /**
     * 회원 정보 조회
     */
    suspend fun getMemberInfo2(): NetworkResult<MemberInfoResponse>
    /**
     * NFT 지갑 발급
     */
    suspend fun getNftWallet(): NetworkResult<NftWalletDto>
    /**
     * 선물 수락
     */
    suspend fun acceptGift(@Path("transferId") transferId: Int, @Path("nftId") nftId: Int): NetworkResult<NftListDto>
    /**
     * 선물 거부
     */
    suspend fun rejectGift(@Path("transferId") transferId: Int, @Path("nftId") nftId: Int): NetworkResult<String>
}