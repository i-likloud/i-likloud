package com.ssafy.likloud.data.model


import com.google.gson.annotations.SerializedName

/**
 * wallet 조회할 때 사용
 */
data class UserInfoDto(

    @SerializedName("email")
    val email: String,

    @SerializedName("goldCoin")
    val goldCoin: Int,

    @SerializedName("memberId")
    val memberId: Int,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("profileAccessory")
    val profileAccessory: Int,

    @SerializedName("profileColor")
    val profileColor: Int,

    @SerializedName("profileFace")
    val profileFace: Int,

    @SerializedName("role")
    val role: String,

    @SerializedName("silverCoin")
    val silverCoin: Int,

    @SerializedName("socialType")
    val socialType: String
)