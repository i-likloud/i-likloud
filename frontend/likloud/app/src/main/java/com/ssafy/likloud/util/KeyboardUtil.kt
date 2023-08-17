package com.ssafy.likloud.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R

private const val TAG = "KeyboardUtil_싸피"
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

/**
 * EditText에 포커스가 있는 상태에서 fragmentLayout(전체 레이아웃)이 눌리면 키보드를 내립니다
 */
@SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
fun initEditText(
    editText: EditText,
    edittextLayout: View?,
    fragmentLayout: View,
    context: Activity,
    setMessage: ((String) -> Unit)?
) {

    editText.onFocusChangeListener = View.OnFocusChangeListener { view, gainFocus ->
        if (edittextLayout == null) return@OnFocusChangeListener
        if (gainFocus) {
            edittextLayout.background =
                context.getDrawable(R.drawable.frame_rounded_border_radius20_stroke3)
        } else {
            edittextLayout.background =
                context.getDrawable(R.drawable.frame_rounded_border_radius20)
        }
    }

    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (setMessage != null) {
                setMessage(s.toString())
            } // setMessage 파라미터를 사용하여 적절한 메시지 설정 함수 호출
        }

        override fun afterTextChanged(s: Editable?) {}
    })

    fragmentLayout.setOnTouchListener { _, _ ->
        Log.d(TAG, "initEditText: ddd")
        context.hideKeyboard()
        editText.clearFocus()
        false
    }
}