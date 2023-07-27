package com.ssafy.likloud.util

import android.content.Context
import android.content.SharedPreferences
import com.ssafy.likloud.ApplicationClass

class SharedPreferencesUtil(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(ApplicationClass.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun addUserAccessToken() {

    }

    fun addUserCookie(cookies: HashSet<String>) {
        val editor = preferences.edit()
        editor.putStringSet(ApplicationClass.COOKIES_KEY_NAME, cookies)
        editor.apply()
    }

//    fun addUser(user : User){
//        val editor = preferences.edit()
//        editor.putString("userid", user.userid)
//        editor.putString("nickname", user.nickname)
//        editor.apply()
//    }

//    fun getUserid() : String {
//       val id = preferences.getString("userid", "")
//        return id!!
//    }

    fun deleteUser() {
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    fun addUserByIdAndNickname(userId : String, passWord : String){
        val editor = preferences.edit()
        editor.putString("userid", userId)
        editor.putString("nickname", passWord)
        editor.apply()
    }


    fun getUserCookie(): MutableSet<String>? {
        return preferences.getStringSet(ApplicationClass.COOKIES_KEY_NAME, HashSet())
    }

    fun getString(key:String): String? {
        return preferences.getString(key, null)
    }
}