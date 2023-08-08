package com.ssafy.likloud.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.FragmentUploadBinding
import com.ssafy.likloud.databinding.ModalChooseGalleryCameraBinding
import com.ssafy.likloud.databinding.ModalLogoutCheckBinding
import com.ssafy.likloud.databinding.ModalSettingsBinding


class LogoutDialog(
    private val logout: () -> Unit,
) : BaseDialog<ModalLogoutCheckBinding>(ModalLogoutCheckBinding::bind, R.layout.modal_logout_check) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    override fun initListener() {
        binding.buttonLogoutYes.setOnClickListener {
            logout.invoke()
            dismiss()
        }

        binding.buttonLogoutNo.setOnClickListener {
            dismiss()
        }
    }
}
