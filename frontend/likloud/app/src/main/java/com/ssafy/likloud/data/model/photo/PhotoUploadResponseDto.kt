package com.ssafy.likloud.data.model.photo


import com.google.gson.annotations.SerializedName

data class PhotoUploadResponseDto(
    @SerializedName("memberId")
    val memberId: Int,
    @SerializedName("photoFileId")
    val photoFileId: Int,
    @SerializedName("photoId")
    val photoId: Int,
    @SerializedName("photoUrl")
    val photoUrl: String
)