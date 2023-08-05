package com.ssafy.likloud.ui.nftlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.databinding.FragmentNftListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "차선호"
@AndroidEntryPoint
class NftListFragment : BaseFragment<FragmentNftListBinding>(
    FragmentNftListBinding::bind, R.layout.fragment_nft_list) {

    private val nftListFragmentViewModel : NftListFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var nftListAdapter: NftListAdapter


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
        nftListFragmentViewModel.getMyNftDtoList()
        initNftListRecyclerView()
    }

    override fun initListener() {
        binding.apply {
            buttonMyNft.setOnClickListener {

            }
            buttonGiftNft.setOnClickListener {

            }
        }
    }

    private fun initObserver(){
        nftListFragmentViewModel.myNftDtoList.observe(viewLifecycleOwner){
            nftListAdapter.submitList(it)
            Log.d(TAG, "nftList : $it")
        }
    }

    private fun initNftListRecyclerView(){
        nftListAdapter = NftListAdapter(mainActivity)
        binding.recyclerviewNft.apply {
            this.adapter = nftListAdapter
            layoutManager = GridLayoutManager(mainActivity,3)
        }
    }


}