package com.ssafy.likloud.ui.upload

import android.os.Bundle
import android.view.View
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.FragmentUploadBinding
import com.ssafy.likloud.databinding.ModalChooseGalleryCameraBinding


class CameraDialog(
//    private val alertDialogModel: RegisterAlertDialogModel,
    private val clickGallery: () -> Unit,
    private val clickCamera: () -> Unit,
) : BaseDialog<ModalChooseGalleryCameraBinding>(ModalChooseGalleryCameraBinding::bind, R.layout.modal_choose_gallery_camera) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    override fun initListener() {
        binding.buttonGallery.setOnClickListener {
            clickGallery.invoke()
            dismiss()
        }

        binding.buttonCamera.setOnClickListener {
            clickCamera.invoke()
            dismiss()
        }
    }

}
