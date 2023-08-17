package com.ssafy.likloud.data.model


import com.google.gson.annotations.SerializedName

data class ReportDto(
    @SerializedName("content")
    val content: String,
    @SerializedName("drawingId")
    val drawingId: Int,
    @SerializedName("reportId")
    val reportId: Int,
    @SerializedName("reportMemberId")
    val reportMemberId: Int
)