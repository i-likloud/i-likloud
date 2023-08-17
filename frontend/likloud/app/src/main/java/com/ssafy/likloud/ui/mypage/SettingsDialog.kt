package com.ssafy.likloud.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.FragmentUploadBinding
import com.ssafy.likloud.databinding.ModalChooseGalleryCameraBinding
import com.ssafy.likloud.databinding.ModalSettingsBinding
import com.ssafy.likloud.ui.nftlist.NftGiftSearchUserFragment


class SettingsDialog(
//    private val alertDialogModel: RegisterAlertDialogModel,
    private val clickBgmToggle: () -> Unit,
    private val logout: () -> Unit,
    private val quitUser : () -> Unit,
    private val openSourceLicenses: () -> Unit,
    private val bgmText : String
) : BaseDialog<ModalSettingsBinding>(ModalSettingsBinding::bind, R.layout.modal_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBgmToggle.text = bgmText
        initListener()
    }

    override fun initListener() {
        binding.buttonBgmToggle.setOnClickListener {
            if (binding.buttonBgmToggle.text == getString(R.string.bgm_off)) {
                binding.buttonBgmToggle.text = getString(R.string.bgm_on)
            } else {
                binding.buttonBgmToggle.text = getString(R.string.bgm_off)
            }
            clickBgmToggle.invoke()
        }
        binding.buttonLogout.setOnClickListener {
            logout.invoke()
        }

        binding.buttonDeleteUser.setOnClickListener {
            openSourceLicenses.invoke()
        }

        binding.buttonQuit.setOnClickListener {
            quitUser.invoke()
        }

        binding.buttonInfo.setOnClickListener {
            val parentFragment = parentFragment
            if (parentFragment is MypageFragment) {
                parentFragment.goInfoFragment()
            }
            dismiss()
        }
    }
}
