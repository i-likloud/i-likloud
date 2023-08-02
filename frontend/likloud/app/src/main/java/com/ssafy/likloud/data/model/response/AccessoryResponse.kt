package com.ssafy.likloud.data.model.response


import com.google.gson.annotations.SerializedName

data class AccessoryResponse(
    @SerializedName("accessoryId")
    val accessoryId: Int,

    @SerializedName("accessoryName")
    val accessoryName: String,

    @SerializedName("accessoryPrice")
    val accessoryPrice: Int
)