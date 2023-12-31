package com.ssafy.likloud.data.model


import com.google.gson.annotations.SerializedName

data class NftListDto(
    @SerializedName("content")
    val content: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("nftId")
    val nftId: Int,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("tokenId")
    val tokenId: String
)

