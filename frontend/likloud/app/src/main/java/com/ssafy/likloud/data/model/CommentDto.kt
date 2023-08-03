package com.ssafy.likloud.data.model

import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("commentId")
    val commentId: Int,

    @SerializedName("content")
    val content: String,

    @SerializedName("commentMember")
    val commentMember: String,

    @SerializedName("drawingId")
    val drawingId: Int,

    @SerializedName("memberId")
    val memberId: Int,

    @SerializedName("createdAt")
    val createdAt: String
    )