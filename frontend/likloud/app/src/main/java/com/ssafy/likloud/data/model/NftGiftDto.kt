package com.ssafy.likloud.data.model


import com.google.gson.annotations.SerializedName
import com.ssafy.likloud.data.model.response.MemberInfoResponse

data class NftGiftDto(
    @SerializedName("content")
    val content: String,
    @SerializedName("mypageInfoDto")
    val memberInfo: MemberInfoResponse,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("nftId")
    val nftId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("tokenId")
    val tokenId: String,
    @SerializedName("transferId")
    val transferId: Int
)