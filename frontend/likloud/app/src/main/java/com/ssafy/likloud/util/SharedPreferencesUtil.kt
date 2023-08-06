package com.ssafy.likloud.util

import android.content.Context
import android.content.SharedPreferences
import com.ssafy.likloud.ApplicationClass

class SharedPreferencesUtil(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(ApplicationClass.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun addUserCookie(cookies: HashSet<String>) {
        val editor = preferences.edit()
        editor.putStringSet(ApplicationClass.COOKIES_KEY_NAME, cookies)
        editor.apply()
    }
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

    fun putString(key:String, token:String) {
        val editor = preferences.edit()
        editor.putString(key, token)
        editor.apply()
    }

    fun setMusicOff() {
        val editor = preferences.edit()
        editor.putBoolean("is_music_played", false)
        editor.apply()
    }

    fun setMusicOn() {
        val editor = preferences.edit()
        editor.putBoolean("is_music_played", true)
        editor.apply()
    }

    fun getMusicStatus() : Boolean{
        return preferences.getBoolean("is_music_played", true)
    }
}