package com.ssafy.likloud.ui.nftlist

import android.os.Bundle
import android.view.View
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.ModalNftGiftBinding
import com.ssafy.likloud.ui.game.GameFragment

class NftGiftDialog(

) : BaseDialog<ModalNftGiftBinding>(ModalNftGiftBinding::bind, R.layout.modal_nft_gift) {
    override fun initListener() {
        binding.apply {
            buttonCancelGift.setOnClickListener {
                dismiss()
            }
            buttonConfirmGift.setOnClickListener {
                val parentFragment = parentFragment
                if (parentFragment is NftGiftSearchUserFragment) {
                    parentFragment.sendGift(binding.edittextMessage.text.toString())
                }
                dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }


}