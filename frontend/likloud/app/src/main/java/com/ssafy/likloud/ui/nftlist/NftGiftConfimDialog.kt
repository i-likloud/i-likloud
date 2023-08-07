package com.ssafy.likloud.ui.nftlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.data.model.NftGiftDto
import com.ssafy.likloud.databinding.ModalNftGiftBinding
import com.ssafy.likloud.databinding.ModalNftGiftConfirmBinding

class NftGiftConfimDialog(
    var nftGiftDto: NftGiftDto
) : BaseDialog<ModalNftGiftConfirmBinding>(ModalNftGiftConfirmBinding::bind, R.layout.modal_nft_gift_confirm) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun initListener() {
        binding.apply {
            buttonAcceptGift.setOnClickListener {
                //선물 수락
                val parentFragment = parentFragment
                if (parentFragment is NftListFragment) {
                    parentFragment.acceptGift(nftGiftDto)
                }
                dismiss()
            }
            buttonRejectGift.setOnClickListener {
                //선물 거부
                val parentFragment = parentFragment
                if (parentFragment is NftListFragment) {
                    parentFragment.rejectGift(nftGiftDto)
                }
                dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initView()
    }

    private fun initView(){

        binding.apply {
            Glide.with(imageMemberColor)
                .load(activityViewModel.waterDropColorList[nftGiftDto.memberInfo.profileColor].resourceId)
                .into(imageMemberColor)
            Glide.with(imageMemberFace)
                .load(activityViewModel.waterDropFaceList[nftGiftDto.memberInfo.profileFace].resourceId)
                .into(imageMemberFace)
            Glide.with(imageMemberAccessory)
                .load(activityViewModel.waterDropAccessoryList[nftGiftDto.memberInfo.profileAccessory].resourceId)
                .into(imageMemberAccessory)
            textNickname.text = nftGiftDto.memberInfo.nickname
            textGiftMessage.text = nftGiftDto.message
        }

    }


}