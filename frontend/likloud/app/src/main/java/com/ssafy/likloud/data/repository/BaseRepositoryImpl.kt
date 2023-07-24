package com.ssafy.likloud.data.repository


import com.ssafy.likloud.data.api.BaseService
import com.ssafy.likloud.data.api.NetworkResult
import com.ssafy.likloud.data.api.handleApi
import com.ssafy.likloud.data.model.SampleDto
import com.ssafy.likloud.data.model.UserDto
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

//    override suspend fun postRefreshToken(refresh_token: String): NetworkResult<LoginResponse> {
//        val body = PostRefreshTokenRequest(refresh_token = refresh_token)
//        return handleApi { mainAPIService.postRefreshToken(body = body).data }
//    }
}