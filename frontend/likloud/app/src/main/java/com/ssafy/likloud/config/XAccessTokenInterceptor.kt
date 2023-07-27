package com.ssafy.likloud.config

import com.ssafy.likloud.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.ssafy.likloud.ApplicationClass.Companion.sharedPreferences
//import com.ssafy.templateapplication.ApplicationClass.Companion.sharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class XAccessTokenInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val jwtToken: String? = sharedPreferences.getString(X_ACCESS_TOKEN)
        if (jwtToken != null) {
            builder.addHeader("Authorization", "Bearer ${jwtToken}")
        }
        return chain.proceed(builder.build())
    }
}