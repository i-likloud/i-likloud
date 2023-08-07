package com.ssafy.likloud.ui.nftlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.NftGiftDto
import com.ssafy.likloud.data.model.NftListDto
import com.ssafy.likloud.databinding.FragmentNftGiftSearchUserBinding
import com.ssafy.likloud.databinding.FragmentNftListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NftGiftSearchUserFragment : BaseFragment<FragmentNftGiftSearchUserBinding>(
    FragmentNftGiftSearchUserBinding::bind, R.layout.fragment_nft_gift_search_user) {

    private val nftGiftSearchUserFragmentViewModel : NftGiftSearchUserFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initListener()
        initObserver()

    }

    private fun init(){
    }

    override fun initListener() {
        binding.apply {
        }
    }

    private fun initObserver(){
    }


}