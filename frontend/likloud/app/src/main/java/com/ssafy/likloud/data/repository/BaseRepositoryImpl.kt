package com.ssafy.likloud.data.repository


import com.ssafy.likloud.ApplicationClass
import com.ssafy.likloud.data.api.BaseService
import com.ssafy.likloud.data.api.NetworkResult
import com.ssafy.likloud.data.api.handleApi
import com.ssafy.likloud.data.model.MemberInfoDto
import com.ssafy.likloud.data.model.PhotoUploadResponseDto
import com.ssafy.likloud.data.model.request.LoginRequest
import com.ssafy.likloud.data.model.response.LoginResponse
import com.ssafy.likloud.data.model.response.ReLoginResponse
import com.ssafy.likloud.data.model.SampleDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.model.request.LoginAdditionalRequest
import com.ssafy.likloud.data.model.request.MemberInfoRequest
import com.ssafy.likloud.data.model.response.MemberInfoResponse
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class BaseRepositoryImpl @Inject constructor(
    @Named("Main")private val baseAPIService: BaseService
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
        return handleApi { baseAPIService.postRefreshToken("Bearer ${ApplicationClass.sharedPreferences.getString("refresh_token")}").body()!! }
    }

    override suspend fun postLogin(loginRequest: LoginRequest): NetworkResult<LoginResponse> {
        return handleApi { baseAPIService.postLogin(loginRequest).body()!! }
    }

    override suspend fun getMemberInfo(memberInfoRequest: MemberInfoRequest): NetworkResult<MemberInfoResponse> {
        return handleApi { baseAPIService.getUserInfo(memberInfoRequest).body()!! }
    }

    override suspend fun patchAdditionalInfo(loginAdditionalRequest: LoginAdditionalRequest): Response<ReLoginResponse> {
        return baseAPIService.patchAdditionalInfo(loginAdditionalRequest)
    }

    override suspend fun postPhotoMultipart(
        multipartBodyPart: List<MultipartBody.Part>,
        memberInfoDto: MemberInfoDto
    ): Response<List<PhotoUploadResponseDto>> {
        return baseAPIService.postPhotoMultipart(multipartBodyPart, memberInfoDto)
    }

}