package com.ssafy.likloud.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 키보드 내리기 코드입니다.
 */
fun Activity.hideKeyboard() {
    if (this.currentFocus != null) {
        val inputManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

/**
 * 키보드 올리기 코드입니다.
 */
fun Activity.showKeyboard(view: View) {
    val inputManager: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)

}