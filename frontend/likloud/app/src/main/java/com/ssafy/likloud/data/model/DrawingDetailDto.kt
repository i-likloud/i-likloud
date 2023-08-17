package com.ssafy.likloud.data.model

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName

data class DrawingDetailDto(

    @SerializedName("drawingId")
    val drawingId: Int,

    @SerializedName("memberId")
    val memberId: Int,

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
    var memberLiked: Boolean,

    @SerializedName("content")
    val content: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("comments")
    val commentList: MutableList<CommentDto>,

    @SerializedName("nftYn")
    val nftYn: Boolean

)