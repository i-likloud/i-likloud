package com.ssafy.likloud.data.model.drawing


import com.google.gson.annotations.SerializedName

data class DrawingUploadResponse(
    @SerializedName("content")
    val content: String,
    @SerializedName("drawingId")
    val drawingId: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("memberId")
    val memberId: Int,
    @SerializedName("photoId")
    val photoId: Int,
    @SerializedName("title")
    val title: String
)