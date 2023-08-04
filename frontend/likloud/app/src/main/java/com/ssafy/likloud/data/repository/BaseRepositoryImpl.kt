package com.ssafy.likloud.data.repository


import com.ssafy.likloud.ApplicationClass
import com.ssafy.likloud.data.api.BaseService
import com.ssafy.likloud.data.api.NetworkResult
import com.ssafy.likloud.data.api.handleApi
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.DrawingDetailDto
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.data.model.MemberInfoDto
import com.ssafy.likloud.data.model.MemberProfileDto
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
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class BaseRepositoryImpl @Inject constructor(
    @Named("Main") private val baseAPIService: BaseService
) : BaseRepository {

    override suspend fun getComment(
        id: Int
    ): NetworkResult<SampleDto> {
//        val body = PostRegisterRequest(nickname, profile_path)
        return handleApi { baseAPIService.getComment(id = id).data }
    }

    override suspend fun getUser(id: Int): NetworkResult<UserDto> {
        return handleApi { baseAPIService.getPosts(id).body()!! }
    }

    override suspend fun postRefreshToken(): NetworkResult<ReLoginResponse> {
        return handleApi {
            baseAPIService.postRefreshToken(
                "Bearer ${
                    ApplicationClass.sharedPreferences.getString(
                        "refresh_token"
                    )
                }"
            ).body()!!
        }
    }

    override suspend fun postLogin(loginRequest: LoginRequest): NetworkResult<LoginResponse> {
        return handleApi { baseAPIService.postLogin(loginRequest).body()!! }
    }

    override suspend fun getMemberInfo(memberInfoRequest: MemberInfoRequest): NetworkResult<MemberInfoResponse> {
        return handleApi { baseAPIService.getMemberInfo(memberInfoRequest).body()!! }
    }

    override suspend fun patchAdditionalInfo(loginAdditionalRequest: LoginAdditionalRequest): Response<ReLoginResponse> {
        return baseAPIService.patchAdditionalInfo(loginAdditionalRequest)
    }

    override suspend fun getDrawingList(orderBy: String): NetworkResult<MutableList<DrawingListDto>> {
        return handleApi { baseAPIService.getDrawingList(orderBy).body()!! }
    }

    override suspend fun getDrawingDetail(drawingId: Int): NetworkResult<DrawingDetailDto> {
        return handleApi { baseAPIService.getDrawingDetail(drawingId).body()!! }
    }

    override suspend fun postPhotoMultipart(
        multipartBodyPart: List<MultipartBody.Part>,
        memberInfoDto: MemberInfoDto
    ): Response<List<PhotoUploadResponseDto>> {
        return baseAPIService.postPhotoMultipart(multipartBodyPart, memberInfoDto)
    }

    override suspend fun postSinglePhotoMultipart(
        multipartBodyPart: MultipartBody.Part,
        memberInfoDto: MemberInfoDto
    ): NetworkResult<List<PhotoUploadResponseDto>> {
        return handleApi {
            baseAPIService.postSinglePhotoMultipart(multipartBodyPart, memberInfoDto).body()!!
        }
    }

    override suspend fun postDrawingMultipart(
        file: MultipartBody.Part,
        photoId: Int,
        title: String,
        content: String,
        memberInfoDto: MemberInfoDto
    ): NetworkResult<DrawingUploadResponse> {
        return handleApi {
            baseAPIService.postDrawingMultipart(
                photoId,
                file,
                title,
                content,
                memberInfoDto
            ).body()!!
        }
    }

    override suspend fun getPhotoList(orderBy: String): NetworkResult<MutableList<PhotoListDto>> {
        return handleApi { baseAPIService.getPhotoList(orderBy).body()!! }
    }

    override suspend fun getMemberProfile(memberId: Int): NetworkResult<MemberProfileDto> {
        return handleApi { baseAPIService.getMemberProfile(memberId).body()!! }
    }

    override suspend fun getPhotoDrawingList(photoId: Int): NetworkResult<MutableList<DrawingListDto>> {
        return handleApi { baseAPIService.getPhotoDrawingList(photoId).body()!! }
    }

    override suspend fun getMyAccessoryList(memberInfoRequest: MemberInfoRequest): NetworkResult<MutableList<AccessoryResponse>> {
        return handleApi { baseAPIService.getMyAccessoryList(memberInfoRequest).body()!! }
    }

    override suspend fun editMyProfile(profileEditRequest: ProfileEditRequest): NetworkResult<MemberInfoResponse> {
        return handleApi { baseAPIService.editMyProfile(profileEditRequest).body()!! }
    }

    override suspend fun plusSilver(): NetworkResult<String> {
        return handleApi { baseAPIService.plusSliver().body()!! }
    }

    override suspend fun getMyDrawingListDtoList(): NetworkResult<MutableList<DrawingListDto>> {
        return handleApi { baseAPIService.getMyDrawingListDtoList().body()!! }
    }

    override suspend fun getLikeDrawingListDtoList(): NetworkResult<MutableList<DrawingListDto>> {
        return handleApi { baseAPIService.getLikeDrawingListDtoList().body()!! }
    }

    override suspend fun getMyPhotoListDtoList(): NetworkResult<MutableList<PhotoListDto>> {
        return handleApi { baseAPIService.getMyPhotoListDtoList().body()!! }
    }

    override suspend fun getStoreInfo(memberInfoRequest: MemberInfoRequest): NetworkResult<StoreResponse> {
        return handleApi { baseAPIService.getStoreInfo(memberInfoRequest).body()!! }
    }

    override suspend fun postBuyAccessory(
        storeId: Int,
        memberInfoRequest: MemberInfoRequest
    ): NetworkResult<StoreItemBuyResponse> {
        return handleApi { baseAPIService.postBuyAccessory(storeId, memberInfoRequest).body()!! }
    }

    override suspend fun getBookmarkPhotoListDtoList(): NetworkResult<MutableList<PhotoListDto>> {
        return handleApi { baseAPIService.getBookmarkPhotoListDtoList().body()!! }
    }

    override suspend fun getPhotoDetail(photoId: Int): NetworkResult<PhotoListDto> {
        return handleApi { baseAPIService.getPhotoDetail(photoId).body()!! }
    }

    override suspend fun changeDrawingLike(drawingId: Int): NetworkResult<String> {
        return handleApi { baseAPIService.changeDrawingLike(drawingId).body()!! }
    }

    override suspend fun changePhotoBookmark(photoId: Int): NetworkResult<String> {
        return handleApi { baseAPIService.changePhotoBookmark(photoId).body()!! }
    }

    override suspend fun registDrawingComment(drawingId: Int, content: String): NetworkResult<CommentDto> {
        return handleApi { baseAPIService.registDrawingComment(drawingId, content).body()!! }
    }

    override suspend fun deleteDrawingComment(commentId: Int): NetworkResult<String> {
        return handleApi { baseAPIService.deleteDrawingComment(commentId).body()!! }
    }
}