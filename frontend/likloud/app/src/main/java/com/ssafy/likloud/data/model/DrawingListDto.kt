package com.ssafy.likloud.data.model

import com.google.gson.annotations.SerializedName

data class DrawingListDto(

    @SerializedName("drawingId")
    val drawingId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("artist")
    val artist: String,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("viewCount")
    val viewCount: Int,

    @SerializedName("likesCount")
    val likesCount: Int,

    @SerializedName("memberLiked")
    val memberLiked: Boolean,

    @SerializedName("memberId")
    val memberId: Int,

    @SerializedName("nftYn")
    val nftYn: Boolean

    )