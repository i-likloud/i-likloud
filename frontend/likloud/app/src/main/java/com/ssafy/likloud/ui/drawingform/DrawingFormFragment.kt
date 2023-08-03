package com.ssafy.likloud.ui.drawingform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentDrawingFormBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrawingFormFragment : BaseFragment<FragmentDrawingFormBinding>(FragmentDrawingFormBinding::bind, R.layout.fragment_drawing_form) {

    override fun initListener() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

}