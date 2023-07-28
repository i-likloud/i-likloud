package com.ssafy.likloud.data.model.response

import com.google.gson.annotations.SerializedName

data class ReLoginResponse (
    @SerializedName("grantType")
    val grantType: String,

    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("accessTokenExpirationPeriod")
    val accessTokenExpirationPeriod: String,
)
