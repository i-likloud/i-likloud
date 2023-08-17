package com.ssafy.likloud.data.model.request

import com.google.gson.annotations.SerializedName

data class LoginAdditionalRequest(
    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("profileColor")
    val profileColor: Int,

    @SerializedName("profileFace")
    val profileFace: Int,

    @SerializedName("profileAccessory")
    val profileAccessory : Int
)