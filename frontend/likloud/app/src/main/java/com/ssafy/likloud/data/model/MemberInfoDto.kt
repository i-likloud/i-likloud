package com.ssafy.likloud.data.model


import com.google.gson.annotations.SerializedName

data class MemberInfoDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("role")
    val role: String
)