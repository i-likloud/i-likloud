package com.ssafy.likloud.data.model.response


import com.google.gson.annotations.SerializedName

data class StoreItemResponse(
    @SerializedName("storeId")
    val storeId: Int,

    @SerializedName("accessoryName")
    val accessoryName: String,

    @SerializedName("accessoryPrice")
    val accessoryPrice: Int,

    @SerializedName("owned")
    val owned: Boolean
)