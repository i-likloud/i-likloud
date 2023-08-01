package com.ssafy.likloud.data.model

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName

data class DrawingDetailDto(

    @SerializedName("drawingId")
    val _id: Long,

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
    val commentList: MutableList<CommentDto>

){
    constructor() : this(0,0,"", "","",0,0,false,"","", mutableListOf())
}