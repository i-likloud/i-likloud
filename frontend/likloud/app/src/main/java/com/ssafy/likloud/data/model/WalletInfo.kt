package com.ssafy.likloud.data.model


import com.google.gson.annotations.SerializedName

data class WalletInfo(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileAccessory")
    val profileAccessory: Int,
    @SerializedName("profileColor")
    val profileColor: Int,
    @SerializedName("profileFace")
    val profileFace: Int
)