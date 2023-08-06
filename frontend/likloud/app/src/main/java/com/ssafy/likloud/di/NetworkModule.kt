package com.ssafy.likloud.di


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssafy.likloud.config.RequestInterceptor
import com.ssafy.likloud.config.ResponseInterceptor
import com.ssafy.likloud.data.api.ApiClient.BASE_URL
import com.ssafy.likloud.data.api.BaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient =

        //DEBUG 모드 인 경우를 생각할꺼면 주석 해제
//        if (BuildConfig.DEBUG) {
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
//            .addInterceptor(EmptyBodyInterceptor())
////            .addInterceptor(BearerInterceptor()) // Refresh Token
//            .addInterceptor(ErrorResponseInterceptor()) // Error Response
//            .build()
//    } else {
        OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(RequestInterceptor()) // JWT 자동 헤더 전송
//            .addInterceptor(EmptyBodyInterceptor())
            .addInterceptor(ResponseInterceptor()) // Refresh Token
//            .addInterceptor(ErrorResponseInterceptor()) // Error Response
//            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
//            .addInterceptor(AddCookiesInterceptor())  //쿠키 전송
//            .addInterceptor(ReceivedCookiesInterceptor()) //쿠키 추출
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
//    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson : Gson = GsonBuilder()
        .setLenient()
        .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
//        .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideMainAPIService(retrofit: Retrofit) : BaseService =
        retrofit.create(BaseService::class.java)



}