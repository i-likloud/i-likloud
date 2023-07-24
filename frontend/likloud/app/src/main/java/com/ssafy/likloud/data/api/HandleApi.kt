package com.ssafy.likloud.data.api


// api try catch 문
internal inline fun <T> handleApi(transform: () -> T): NetworkResult<T> = try {
    NetworkResult.Success(transform.invoke())
} catch (e: Exception) {
    when (e) {
        else -> NetworkResult.Error(e)
    }
}