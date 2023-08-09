package com.ssafy.likloud.ui.nftlist

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.NftGiftDto
import com.ssafy.likloud.data.model.NftListDto
import com.ssafy.likloud.data.model.response.MemberInfoResponse
import com.ssafy.likloud.databinding.FragmentNftGiftSearchUserBinding
import com.ssafy.likloud.databinding.FragmentNftListBinding
import com.ssafy.likloud.ui.drawing.DrawingDetailFragmentArgs
import com.ssafy.likloud.util.initEditText
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "차선호"

@AndroidEntryPoint
class NftGiftSearchUserFragment(var nftId: Int) : BaseFragment<FragmentNftGiftSearchUserBinding>(
    FragmentNftGiftSearchUserBinding::bind, R.layout.fragment_nft_gift_search_user
) {

    private val nftGiftSearchUserFragmentViewModel: NftGiftSearchUserFragmentViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var nftGiftSearchUserAdapter: NftGiftSearchUserAdapter

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

    private fun init() {
        initNftGiftSearchUserRecyclerView()
    }

    override fun initListener() {

        initEditText(
            binding.edittextSearchUser,
            null,
            binding.layoutNftGiftSearchUserFragment,
            mActivity,
            null
        )

        binding.apply {
            edittextSearchUser.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    nftGiftSearchUserFragmentViewModel.getCurrentSearchUserList(p0.toString())
                }

            })
        }
    }

    private fun initObserver() {
        nftGiftSearchUserFragmentViewModel.currentSearchUserList.observe(viewLifecycleOwner) {
            nftGiftSearchUserAdapter.submitList(it)
        }
    }

    private fun initNftGiftSearchUserRecyclerView() {
        nftGiftSearchUserAdapter = NftGiftSearchUserAdapter(activityViewModel)
        binding.recyclerviewSearchUser.apply {
            this.adapter = nftGiftSearchUserAdapter.apply {
                itemClickListner = object : NftGiftSearchUserAdapter.ItemClickListener {
                    override fun onClick(memberInfo: MemberInfoResponse) {
                        //여기서 해당 멤버로 nft를 선물 (args.nftId가 nftId)
                        Log.d(TAG, "onClick.....")
                        nftGiftSearchUserFragmentViewModel.setMemberInfo(memberInfo)
                        nftGiftSearchUserFragmentViewModel.nftGiftDialog.show(
                            childFragmentManager,
                            "input message"
                        )
                    }

                }
            }
            layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun sendGift(message: String) {
        activityViewModel.sendGift(
            nftId,
            nftGiftSearchUserFragmentViewModel.toMemberInfo.memberId,
            message
        )
    }


}