package com.ssafy.likloud.data.model


import com.google.gson.annotations.SerializedName

data class NftWalletDto(
    @SerializedName("address")
    val address: String,
    @SerializedName("keyId")
    val keyId: String,
    @SerializedName("krn")
    val krn: String
)