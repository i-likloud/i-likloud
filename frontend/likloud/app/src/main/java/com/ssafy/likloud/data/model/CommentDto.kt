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

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("profileFace")
    val profileFace: Int,

    @SerializedName("profileColor")
    val profileColor: Int,

    @SerializedName("profileAccessor")
    val profileAccessory: Int,

    @SerializedName("createdAt")
    val createdAt: String
    )