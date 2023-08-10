package com.ssafy.likloud.ui.nftlist

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.ModalNftGiftBinding
import com.ssafy.likloud.ui.game.GameFragment
import com.ssafy.likloud.util.initEditText
import dagger.hilt.android.AndroidEntryPoint
import kakao.k.p
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NftGiftDialog(

) : BaseDialog<ModalNftGiftBinding>(ModalNftGiftBinding::bind, R.layout.modal_nft_gift) {
    private val nftGiftDialogViewModel: NftGiftDialogViewModel by viewModels()
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
                requireActivity()
            ){message->
                nftGiftDialogViewModel.setMessage(message)
            }

            edittextMessage.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard()
                }
            }
        }
    }

    private fun initObserver(){
        viewLifecycleOwner.lifecycleScope.launch {
            nftGiftDialogViewModel.massage.collectLatest {
                binding.textMessageCount.text = "${it.length}${context?.getString(R.string.message_max)}"
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }


}