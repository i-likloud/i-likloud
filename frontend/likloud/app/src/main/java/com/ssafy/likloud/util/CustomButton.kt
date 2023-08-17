package com.ssafy.likloud.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.widget.AppCompatButton
import com.ssafy.likloud.R

class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var button: AppCompatButton

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.button_component, this, true)
        button = view.findViewById(R.id.button_custom)
    }

    fun setText(text: String) {
        button.text = text
    }

    fun clicked(listener: OnClickListener) {
        button.setOnClickListener(listener)
    }

}