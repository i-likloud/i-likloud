package com.ssafy.likloud.ui.nftlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.NftListDto
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
    private lateinit var giftListAdapter: NftGiftAdapter


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
        toggleButton(binding.buttonMyNft)
        initNftListRecyclerView()
    }

    override fun initListener() {
        binding.apply {
            buttonMyNft.setOnClickListener {
                nftListFragmentViewModel.getMyNftDtoList()
                initNftListRecyclerView()
                toggleButton(binding.buttonMyNft)
            }
            buttonGiftNft.setOnClickListener {
                nftListFragmentViewModel.getNftGiftList()
                //선물함 불러온거 viewmodel에서 처리할 필요 있음
                initGiftListRecyclerView()
                toggleButton(binding.buttonGiftNft)
            }
        }
    }

    private fun initObserver(){
        nftListFragmentViewModel.myNftDtoList.observe(viewLifecycleOwner){
            nftListAdapter.submitList(it)
            Log.d(TAG, "nftList : $it")
        }
        nftListFragmentViewModel.giftNftDtoList.observe(viewLifecycleOwner){
            giftListAdapter.submitList(it)
        }
    }

    private fun initNftListRecyclerView(){
        nftListAdapter = NftListAdapter(mainActivity)
        binding.recyclerviewNft.apply {
            this.adapter = nftListAdapter.apply {
                itemClickListner = object: NftListAdapter.ItemClickListener{
                    override fun onClick(nftDto: NftListDto) {
                        //선물하기 로직
                        Toast.makeText(mainActivity, "선물하기 클릭...", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            layoutManager = GridLayoutManager(mainActivity,3)
        }
    }

    private fun initGiftListRecyclerView(){
        giftListAdapter = NftGiftAdapter(mainActivity)
        binding.recyclerviewNft.apply {
            this.adapter = giftListAdapter
            layoutManager = GridLayoutManager(mainActivity,3)
        }
    }

    private fun toggleButton(view: View){
        binding.apply {
            buttonMyNft.background = ContextCompat.getDrawable(mainActivity, R.drawable.button_frame_black)
            buttonGiftNft.background = ContextCompat.getDrawable(mainActivity, R.drawable.button_frame_black)
        }
        view.background = ContextCompat.getDrawable(mainActivity, R.drawable.button_frame_skyblue)
    }

}