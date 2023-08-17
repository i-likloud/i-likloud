package com.ssafy.likloud.data.model.response


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("errorCode")
    val errorCode: String,

    @SerializedName("httpStatus")
    val httpStatus: String,

    @SerializedName("httpStatusCode")
    val httpStatusCode: Int,

    @SerializedName("message")
    val message: String
)