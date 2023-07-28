package com.ssafy.likloud.data.model.request

import com.google.gson.annotations.SerializedName

data class MemberInfoRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("role")
    val role: String = "MEMBER"
)
