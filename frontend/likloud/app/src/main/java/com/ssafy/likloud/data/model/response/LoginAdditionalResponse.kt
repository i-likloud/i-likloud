package com.ssafy.likloud.data.model.response

import com.google.gson.annotations.SerializedName

data class LoginAdditionalResponse(
    @SerializedName("email")
    val email: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("profileColor")
    val profileColor: Int,

    @SerializedName("profileFace")
    val profileFace: Int,

    @SerializedName("role")
    val role: String,

    @SerializedName("socialType")
    val socialType: String
)