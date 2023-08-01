package com.ssafy.likloud.data.repository

import androidx.lifecycle.MutableLiveData
import com.ssafy.likloud.data.api.NetworkResult
import com.ssafy.likloud.data.model.DrawingDetailDto
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.MemberInfoDto
import com.ssafy.likloud.data.model.photo.PhotoUploadResponseDto
import com.ssafy.likloud.data.model.request.LoginRequest
import com.ssafy.likloud.data.model.response.LoginResponse
import com.ssafy.likloud.data.model.response.ReLoginResponse
import com.ssafy.likloud.data.model.SampleDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.model.request.LoginAdditionalRequest
import com.ssafy.likloud.data.model.request.MemberInfoRequest
import com.ssafy.likloud.data.model.response.MemberInfoResponse
import retrofit2.Response
import retrofit2.http.Path
import okhttp3.MultipartBody
import retrofit2.Response

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
        multipartBodyPartList : List<MultipartBody.Part>,
        memberInfoDto: MemberInfoDto
    ) : Response<List<PhotoUploadResponseDto>>


    suspend fun postSinglePhotoMultipart(
        multipartBodyPart : MultipartBody.Part,
        memberInfoDto: MemberInfoDto
    ) : NetworkResult<List<PhotoUploadResponseDto>>
}