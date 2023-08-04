package com.ssafy.likloud.data.model.response


import com.google.gson.annotations.SerializedName

data class StoreItemBuyResponse(
    @SerializedName("profile")
    val memberInfoResponse: MemberInfoResponse,

    @SerializedName("myAccessory")
    val myAccessoryList: MutableList<AccessoryResponse>
)