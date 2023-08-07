package com.ssafy.likloud.data.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("grantType")
    val grantType: String,

    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String,

    @SerializedName("accessTokenExpirationPeriod")
    val accessTokenExpirationPeriod: String,

    @SerializedName("refreshTokenExpirationPeriod")
    val refreshTokenExpirationPeriod: String,

    @SerializedName("role")
    val role: String
)