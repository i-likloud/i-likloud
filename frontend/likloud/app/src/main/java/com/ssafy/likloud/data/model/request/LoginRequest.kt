package com.ssafy.likloud.data.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("socialType")
    val socialType: String
)
