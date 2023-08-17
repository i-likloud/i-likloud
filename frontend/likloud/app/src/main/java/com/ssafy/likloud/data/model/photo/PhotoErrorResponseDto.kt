package com.ssafy.likloud.data.model.photo


import com.google.gson.annotations.SerializedName

data class PhotoErrorResponseDto(
    @SerializedName("message")
    val message: String,
    @SerializedName("photoUrl")
    val photoUrl: String
)