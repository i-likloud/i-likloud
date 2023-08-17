package com.ssafy.likloud.ui.nftlist

import android.os.Bundle
import android.view.View
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.ModalNftGiftBinding
import com.ssafy.likloud.databinding.ModalNftInfoBinding

class NftInfoDialog (

) : BaseDialog<ModalNftInfoBinding>(ModalNftInfoBinding::bind, R.layout.modal_nft_info) {
    override fun initListener() {
        binding.apply {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }


}