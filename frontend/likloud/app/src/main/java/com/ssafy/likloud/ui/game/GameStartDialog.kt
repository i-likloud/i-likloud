package com.ssafy.likloud.ui.game

import android.content.Context
import android.os.Bundle
import android.view.View
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.ModalGameStartBinding
import com.ssafy.likloud.ui.drawing.DrawingDetailFragment

class GameStartDialog (

) : BaseDialog<ModalGameStartBinding>(ModalGameStartBinding::bind, R.layout.modal_game_start) {

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun initListener() {
        binding.apply {
            buttonGameStart.setOnClickListener {
                val parentFragment = parentFragment
                if (parentFragment is GameFragment) {
                    parentFragment.gameStart()
                }
                dismiss()
            }
            buttonCancel.setOnClickListener {
                val parentFragment = parentFragment
                if (parentFragment is GameFragment) {
                    parentFragment.goHomefragment()
                }
                dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }
}