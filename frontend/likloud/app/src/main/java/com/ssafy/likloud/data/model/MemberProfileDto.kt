package com.ssafy.likloud.data.model

import com.google.gson.annotations.SerializedName

data class MemberProfileDto(

    @SerializedName("nickname")
    var nickname: String,

    @SerializedName("profileColor")
    var profileColor: Int,

    @SerializedName("profileFace")
    var profileFace: Int,

    @SerializedName("profileAccessory")
    var profileAccessory: Int

)