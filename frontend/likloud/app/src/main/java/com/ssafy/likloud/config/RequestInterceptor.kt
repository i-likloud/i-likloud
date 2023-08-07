package com.ssafy.likloud.config

import android.util.Log
import com.ssafy.likloud.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.ssafy.likloud.ApplicationClass.Companion.sharedPreferences
//import com.ssafy.templateapplication.ApplicationClass.Companion.sharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.Exception

private const val TAG = "RequestInterceptor_싸피"
class RequestInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        Log.d(TAG, "intercept RequestInterceptor: ${sharedPreferences.getString(X_ACCESS_TOKEN)}")
        try {
            sharedPreferences.getString(X_ACCESS_TOKEN).let { token ->
                token?.let {
                    builder.addHeader("Authorization", "Bearer $token")
                    Log.d(TAG, "intercept: JWT AccessToken 헤더에 담았습니다.")
                    return chain.proceed(builder.build())
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "intercept: 토큰 만료 신호")
            return chain.proceed(chain.request())
        }
        return chain.proceed(chain.request())
    }

}