package com.ssafy.likloud.data.model.response


import com.google.gson.annotations.SerializedName

data class StoreResponse(
    @SerializedName("profile")
    val memberInfoResponse: MemberInfoResponse,

    @SerializedName("allAccessoriesWithOwnership")
    val storeItemList: MutableList<StoreItemResponse>
)