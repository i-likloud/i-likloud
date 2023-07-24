package com.ssafy.likloud.base

import com.google.gson.annotations.SerializedName

// 반복되는 리스폰스 내용 중복을 줄이기 위해 사용. 리스폰스 데이터 클래스를 만들때 상속해서 사용합니다.
// 실 서버가 어떻게 데이터를 주느냐에 따라 다르게 해야될 수 있으므로 커스텀 해서 만들어야 함!!!!!
data class BaseResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: T,
    @SerializedName("time_stamp") val time_stamp: String
)