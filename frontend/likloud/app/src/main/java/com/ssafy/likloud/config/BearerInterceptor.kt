package com.ssafy.likloud.config

import android.util.Log
import com.ssafy.likloud.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.ssafy.likloud.ApplicationClass.Companion.X_REFRESH_TOKEN
import com.ssafy.likloud.ApplicationClass.Companion.sharedPreferences
import com.ssafy.likloud.data.api.ApiClient.BASE_URL
import com.ssafy.likloud.data.api.BaseService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


/**
 * bearer 토큰 필요한 api 사용시 accessToken유효한지 검사
 * 유효하지 않다면 재발급 api 호출
 * refreshToken이 유효하다면 정상적으로 accessToken재발급 후 기존 api 동작 완료
*/
private const val TAG = "BearerInterceptor_싸피_http"
class BearerInterceptor: Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        var accessToken = ""
        var isRefreshable = false

        if(response.code == 401) {
            Log.d(TAG, "intercept: 토큰이 만료되어서 다시 보내용")
            runBlocking {
                //토큰 갱신 api 호출
                sharedPreferences.getString(X_REFRESH_TOKEN)?.let {
                    sharedPreferences.putString(X_ACCESS_TOKEN, it)

                    val result = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(BaseService::class.java).postRefreshToken()

                    Log.d(TAG, "intercept: 저장되어 있는 리프레시 토큰 ${it}")
                    Log.d(TAG, "intercept: ${result.body()}")

                    if(result.isSuccessful) {
                        sharedPreferences.putString("access_token", result.body()!!.accessToken)
                        Log.d(TAG, "intercept: 만료된 토큰 다시 받은거 ${result.body()!!.accessToken}")
//                        sharedPreferences.putString("refresh_token", result.data.refreshToken)
                        accessToken = result.body()!!.accessToken
                        isRefreshable = true
                    }
                    if (result.body() == null){
                        Log.d(TAG, "intercept: 만료된 토큰 다시 받은거 에러!!! ${result.body()!!.accessToken}")
                    }
                }
            }
        }

        if(isRefreshable) {
            val newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer $accessToken").build()
            return chain.proceed(newRequest)
        }

        return response
    }
}