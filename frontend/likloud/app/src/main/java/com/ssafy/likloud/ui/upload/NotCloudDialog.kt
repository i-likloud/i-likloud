package com.ssafy.likloud.ui.upload

import android.os.Bundle
import android.view.View
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.FragmentUploadBinding
import com.ssafy.likloud.databinding.ModalChooseGalleryCameraBinding
import com.ssafy.likloud.databinding.ModalNotCloudBinding

class NotCloudDialog(
    private val errorMessage: String,
    private val clickTakePhotoAgain: () -> Unit,
) : BaseDialog<ModalNotCloudBinding>(ModalNotCloudBinding::bind, R.layout.modal_not_cloud) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        binding.textAlertDescription.text = errorMessage
    }

    override fun initListener() {
        binding.buttonAgainPhoto.setOnClickListener {
            clickTakePhotoAgain.invoke()
            dismiss()
        }

        binding.buttonDismiss.setOnClickListener {
            dismiss()
        }

    }
}
