package com.ssafy.likloud.ui.drawing

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.ModalDrawingDeleteBinding
import com.ssafy.likloud.databinding.ModalDrawingReportBinding
import com.ssafy.likloud.util.initEditText

class DrawingDeleteDialog(
    private val delete: () -> Unit
) : BaseDialog<ModalDrawingDeleteBinding>(ModalDrawingDeleteBinding::bind, R.layout.modal_drawing_delete) {

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun initListener() {
        binding.apply {
            buttonDelete.setOnClickListener {
                delete.invoke()
                dismiss()
            }
            buttonCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        init()
    }

    private fun init(){

    }


}