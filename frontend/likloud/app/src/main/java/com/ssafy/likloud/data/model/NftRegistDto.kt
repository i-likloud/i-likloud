package com.ssafy.likloud.data.model


import com.google.gson.annotations.SerializedName

data class NftRegistDto(
    @SerializedName("drawingId")
    val drawingId: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("metadata")
    val metadata: String,
    @SerializedName("nftId")
    val nftId: Int,
    @SerializedName("ownerId")
    val ownerId: Int
)