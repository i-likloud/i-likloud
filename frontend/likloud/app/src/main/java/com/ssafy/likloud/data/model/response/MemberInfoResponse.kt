package com.ssafy.likloud.data.model.response

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.google.gson.annotations.SerializedName

data class MemberInfoResponse(
    @SerializedName("email")
    val email: String,

    @SerializedName("goldCoin")
    val goldCoin: Int,

    @SerializedName("memberId")
    val memberId: Int,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("profileColor")
    val profileColor: Int,

    @SerializedName("profileFace")
    val profileFace: Int,

    @SerializedName("role")
    val role: String,

    @SerializedName("socialType")
    val socialType: String
)