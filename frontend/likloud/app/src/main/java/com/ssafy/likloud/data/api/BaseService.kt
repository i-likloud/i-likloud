package com.ssafy.likloud.data.api

import com.ssafy.likloud.base.BaseResponse
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
import com.ssafy.likloud.data.model.request.LoginAdditionalRequest
import com.ssafy.likloud.data.model.request.MemberInfoRequest
import com.ssafy.likloud.data.model.response.MemberInfoResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface BaseService {

    //BaseResponse 객체로 return 하면 오류남.. 왜???
    @GET("posts/{id}")
    suspend fun getPosts(@Path("id") id: Int): Response<UserDto>

    @GET("api/example/{id}")
    suspend fun getComment(@Path("id") id: Int): BaseResponse<SampleDto>

    // JWT 토큰 재발급
//    @Headers()
    @POST("api/accounts/access-token/re")
    suspend fun postRefreshToken(@Header("Authorization") Authorization: String): Response<ReLoginResponse>

    // 회원가입 / (로그인)
    @POST("api/oauth/login")
    suspend fun postLogin(@Body body: LoginRequest): Response<LoginResponse>

    // 유저 정보를 받아온다.
    @GET("api/mypage/home")
    suspend fun getMemberInfo(@Query("body") body: MemberInfoRequest): Response<MemberInfoResponse>

    // MEMBER로 올리고, newToken을 헤더에 담아 받아온다.
    @PATCH("api/member/additional")
    suspend fun patchAdditionalInfo(@Body body: LoginAdditionalRequest): Response<ReLoginResponse>

    /**
     * 그림 게시물 조회
     * orderBy에 "?orderBy=likesCount"(좋아요 순), "?orderBy=viewCount"(조회수 순)
     */
    @GET("api/drawings/")
    suspend fun getDrawingList(@Query("orderBy") orderBy: String): Response<MutableList<DrawingListDto>>

    /**
     * 그림 상세 조회 함수(인자로 DrawingListDto의 _id값 넣으면 됨)
     */
    @GET("api/drawings/{drawingId}")
    suspend fun getDrawingDetail(@Path("drawingId") drawingId: Int): Response<DrawingDetailDto>

    @Multipart
    @POST("api/photo/upload")
    suspend fun postPhotoMultipart(
        @Part multipartFiles: List<MultipartBody.Part>,
        @Part ("memberInfoDto") memberInfoDto: MemberInfoDto
    ): Response<List<PhotoUploadResponseDto>>

    @Multipart
    @POST("api/photo/upload")
    suspend fun postSinglePhotoMultipart(
        @Part multipartFiles: MultipartBody.Part,
        @Part("memberInfoDto") memberInfoDto: MemberInfoDto
    ): Response<List<PhotoUploadResponseDto>>

    /**
     * 사진 게시물 조회
     * parameter로 new(최신순)-기본 / pick(그린 수 순) / bookmarkdesc(즐찾순)
     */
    @GET("api/photo/{orderBy}")
    suspend fun getPhotoList(@Path("orderBy") orderBy: String): Response<MutableList<PhotoListDto>>

    /**
     * 특정 멤버 프로필 조회
     */
    @GET("api/member/search/{memberId}")
    suspend fun getMemberProfile(@Path("memberId") memberId: Int): Response<MemberProfileDto>

    /**
     * 해당 사진에 대한 그림 목록 조회
     */
    @GET("api/photo/{photoId}/alldrawings")
    suspend fun getPhotoDrawingList(@Path("photoId") photoId: Int): Response<MutableList<DrawingListDto>>
}

//api 만드는 과정
//1. api service 작성 (BaseService 참고)S
//2. api service를 사용하는 Repository Interface 생성 (BaseRepository 참고)
//3. repository Interface 구현체인 Impl 파일 생성 (BaseRepositoryImpl 참고)
//4. respository module에 내가 만든 repository 등록 (RepositoryModule 참고)

//5. 뷰모델에서 불러와 사용 (HomeFragmentViewModel 참고)