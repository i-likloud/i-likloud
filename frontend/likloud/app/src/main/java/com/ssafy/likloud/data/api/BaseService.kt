package com.ssafy.likloud.data.api

import com.ssafy.likloud.base.BaseResponse
import com.ssafy.likloud.data.model.SampleDto
import com.ssafy.likloud.data.model.UserDto
import retrofit2.Response
import retrofit2.http.*


interface BaseService {

    //BaseResponse 객체로 return 하면 오류남.. 왜???
    @GET("posts/{id}")
    suspend fun getPosts(@Path("id") id: Int): Response<UserDto>

    @GET("api/example/{id}")
    suspend fun getComment(@Path("id") id: Int): BaseResponse<SampleDto>

}

//api 만드는 과정
//1. api service 작성 (BaseService 참고)
//2. api service를 사용하는 Repository Interface 생성 (BaseRepository 참고)
//3. repository Interface 구현체인 Impl 파일 생성 (BaseRepositoryImpl 참고)
//4. respository module에 내가 만든 repository 등록 (RepositoryModule 참고)

//5. 뷰모델에서 불러와 사용 (HomeFragmentViewModel 참고)