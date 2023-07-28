package com.ssafy.likloud

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.ssafy.likloud.data.api.ApiClient.NATIVE_APP_KEY
import com.ssafy.likloud.util.SharedPreferencesUtil
import dagger.hilt.android.HiltAndroidApp
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@HiltAndroidApp
// 앱이 실행될때 1번만 실행이 됩니다.
class ApplicationClass : Application() {
    // 코틀린의 전역변수
    companion object {
         //만들어져있는 SharedPreferences 를 사용해야합니다. 재생성하지 않도록 유념해주세요
        lateinit var sharedPreferences: SharedPreferencesUtil

         //JWT Token Header 키 값
        const val X_ACCESS_TOKEN = "access_token"
        const val X_REFRESH_TOKEN = "refresh_token"
        const val SHARED_PREFERENCES_NAME = "SSAFY_TEMPLATE_APP"
        const val COOKIES_KEY_NAME = "cookies"
    }

   // 앱이 처음 생성되는 순간, SP를 새로 만들어준다.
    override fun onCreate() {
        super.onCreate()
        getHashKey()
        sharedPreferences = SharedPreferencesUtil(applicationContext)
       KakaoSdk.init(this, NATIVE_APP_KEY)
    }

    // 카카오 로그인을 위해 해시키를 발급합니다.
    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))

            } catch (e: NoSuchAlgorithmException) {
            }
        }
    }
}