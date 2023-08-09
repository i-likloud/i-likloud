package com.ssafy.likloud.ui.nftlist

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.ModalNftGiftBinding
import com.ssafy.likloud.ui.game.GameFragment
import com.ssafy.likloud.util.initEditText

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

            initEditText(
                edittextMessage,
                null,
                binding.root,
                requireActivity(),
                null
            )

            edittextMessage.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }


}