package com.ssafy.likloud.util

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.ssafy.likloud.databinding.DialogLoadingBinding
import com.ssafy.likloud.databinding.ModalLoadingBinding

//필요한 경우 LoadingDialog XML 만들어 사용

class LoadingDialog(context: Context) : Dialog(context) {
    private lateinit var binding: ModalLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ModalLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable())
        window!!.setDimAmount(0.6f)
    }

    override fun show() {
        if(!this.isShowing) super.show()
    }
}