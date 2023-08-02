package com.ssafy.likloud.data.model.response


import com.google.gson.annotations.SerializedName

data class MemberInfoResponse(
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

    @SerializedName("silverCoin")
    val silverCoin: Int
)