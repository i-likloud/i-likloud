package com.ssafy.likloud.ui.game

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.ModalFailGameBinding

private const val TAG = "차선호"
class FailGameDialog (

) : BaseDialog<ModalFailGameBinding>(ModalFailGameBinding::bind, R.layout.modal_fail_game) {
    override fun initListener() {
        binding.apply {
            buttonGoHome.setOnClickListener {
                val parentFragment = parentFragment
                if (parentFragment is GameFragment) {
                    parentFragment.changeNextFragment("home")
                }
                dismiss() // 다이얼로그 닫기
            }
            buttonReGame.setOnClickListener {
                val parentFragment = parentFragment
                if (parentFragment is GameFragment) {
                    parentFragment.changeNextFragment("game")
                }
                dismiss() // 다이얼로그 닫기
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }


}