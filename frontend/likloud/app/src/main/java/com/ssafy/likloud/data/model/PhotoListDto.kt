package com.ssafy.likloud.data.model

import com.google.gson.annotations.SerializedName

data class PhotoListDto(

    @SerializedName("photoId")
    var photoId: Int,

    @SerializedName("memberId")
    var memberId: Int,

    @SerializedName("photoUrl")
    var photoUrl: String

    )