package com.ssafy.likloud.data.model.request


import com.google.gson.annotations.SerializedName

data class ProfileEditRequest(
    @SerializedName("profileColor")
    val profileColor: Int,

    @SerializedName("profileFace")
    val profileFace: Int,

    @SerializedName("profileAccessory")
    val profileAccessory: Int
)