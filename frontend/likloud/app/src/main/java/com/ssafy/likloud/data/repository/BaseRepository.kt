package com.ssafy.likloud.data.repository

import com.ssafy.likloud.data.api.NetworkResult
import com.ssafy.likloud.data.model.SampleDto
import com.ssafy.likloud.data.model.UserDto

interface BaseRepository {

    // 등록 요청
    suspend fun getComment(
        id: Int
    ): NetworkResult<SampleDto>

    suspend fun getUser(
        id: Int
    ): NetworkResult<UserDto>

    // 토큰 리프래쉬
//    suspend fun postRefreshToken(refresh_token: String): NetworkResult<Int>

}